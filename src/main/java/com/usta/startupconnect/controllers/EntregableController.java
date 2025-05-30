package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EntregableEntity;
import com.usta.startupconnect.models.services.EntregablesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller
public class EntregableController {

    @Autowired
    private EntregablesService entregablesService;
    
    @Autowired
    private com.usta.startupconnect.models.services.TareaService tareaService;

    @GetMapping("/entregable")
    public String listarEtregables(Model model) {
        model.addAttribute("title", "Entregables");
        model.addAttribute("urlRegistro", "/crearEntregable");
        List<EntregableEntity> lista = entregablesService.findAll();
        lista.sort(Comparator.comparing(EntregableEntity::getId));
        model.addAttribute("entregables", lista);
        return "/entregable/listarEntregables";
    }

    @GetMapping("/crearEntregable")
    public String crearEntregable(Model model, @RequestParam(name = "idTarea", required = false) Long idTarea) {
        model.addAttribute("title", "Crear Entregable");
        EntregableEntity entregable = new EntregableEntity();
        
        // Obtener todas las tareas para el dropdown
        model.addAttribute("tareas", tareaService.findAll());
        
        // Si viene un ID de tarea desde la página de detalle
        if (idTarea != null) {
            // Buscar la tarea por ID
            com.usta.startupconnect.entities.TareaEntity tarea = tareaService.findById(idTarea);
            if (tarea != null) {
                // Establecer el ID de la tarea en el entregable
                entregable.setIdTarea(idTarea);
                // Pasar el nombre de la tarea para mostrarlo en el input de solo lectura
                model.addAttribute("tareaNombre", tarea.getTitulo());
            }
        }
        
        model.addAttribute("entregable", entregable);
        return "/entregable/crearEntregable";
    }

    @PostMapping("/guardarEntregable")
    public String guardarEntregable(@ModelAttribute EntregableEntity entregable,
                                    @RequestParam("archivo") MultipartFile archivo,
                                    RedirectAttributes redirectAttributes) {
        try {
            if (archivo.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar un archivo.");
                return "redirect:/crearEntregable";
            }

            // Crear carpeta si no existe
            String uploadDir = "src/main/resources/uploads/entregables/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // Guardar archivo
            String filename = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
            Path path = Paths.get(uploadDir + filename);
            Files.write(path, archivo.getBytes());

            // Configurar entregable
            entregable.setNombreArchivo(archivo.getOriginalFilename());
            entregable.setRutaArchivo(path.toString());
            entregable.setEstado("SUBIDO");
            
            // Si no se establece manualmente, establecer estado por defecto
            if (entregable.getEstado() == null || entregable.getEstado().isEmpty()) {
                entregable.setEstado("Pendiente");
            }

            // Verificar que tenemos el ID de la tarea
            if (entregable.getIdTarea() == null) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar una tarea.");
                return "redirect:/crearEntregable";
            }

            entregablesService.save(entregable);

            redirectAttributes.addFlashAttribute("success", "Archivo subido correctamente.");
            return "redirect:/entregable";

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al subir el archivo.");
            return "redirect:/crearEntregable";
        }
    }

    @GetMapping("/descargarEntregable/{id}")
    public ResponseEntity<Resource> descargarEntregable(@PathVariable Long id) {
        EntregableEntity entregable = entregablesService.findById(id);

        if (entregable == null || entregable.getRutaArchivo() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path path = Paths.get(entregable.getRutaArchivo());
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + entregable.getNombreArchivo() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/previewEntregable/{id}")
    public ResponseEntity<?> previewEntregable(@PathVariable Long id) {
        EntregableEntity entregable = entregablesService.findById(id);

        if (entregable == null || entregable.getRutaArchivo() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path path = Paths.get(entregable.getRutaArchivo());
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = determineContentType(entregable.getNombreArchivo());
            
            // For text files, HTML, and JSON, we want to display them directly in the browser
            if (contentType.startsWith("text/") || 
                contentType.equals("application/json") ||
                contentType.equals("text/html") ||
                contentType.equals("text/javascript")) {
                    return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + entregable.getNombreArchivo() + "\"")
                        .body(resource);
            }
            
            // For PDFs and images, also display inline
            if (contentType.equals("application/pdf") || contentType.startsWith("image/")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + entregable.getNombreArchivo() + "\"")
                        .body(resource);
            }

            // For all other types, default to download
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + entregable.getNombreArchivo() + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private String determineContentType(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1).toLowerCase();
        }
        
        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            case "svg":
                return "image/svg+xml";
            case "txt":
            case "log":
            case "md":
            case "csv":
                return "text/plain";
            case "html":
            case "htm":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "json":
                return "application/json";
            case "xml":
                return "application/xml";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "mp4":
                return "video/mp4";
            case "webm":
                return "video/webm";
            case "mp3":
                return "audio/mpeg";
            case "wav":
                return "audio/wav";
            case "zip":
                return "application/zip";
            case "rar":
                return "application/x-rar-compressed";
            case "7z":
                return "application/x-7z-compressed";
            case "tar":
                return "application/x-tar";
            default:
                return "application/octet-stream";
        }
    }
}
