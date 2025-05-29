package com.usta.startupconnect.models.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.usta.startupconnect.exception.MeetingCreationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleMeetService {
    
    private static final Logger logger = LoggerFactory.getLogger(GoogleMeetService.class);
    private static final String APPLICATION_NAME = "StartUpConnect Google Meet Integration";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials/credentials.json";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Value("${google.calendar.timezone:America/Bogota}")
    private String timeZone;

    /**
     * Creates a Google Meet event with the specified details.
     *
     * @param summary     The title of the meeting
     * @param description The description of the meeting
     * @param startTime   ISO-8601 formatted start time
     * @param endTime     ISO-8601 formatted end time
     * @return The created Event with conference details
     * @throws MeetingCreationException if unable to create the meeting
     */
    public Event createMeetEvent(String summary, String description, String startTime, String endTime) {
        try {
            // Build the Google Calendar service
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // Parse start and end times
            DateTime start = parseDateTime(startTime);
            DateTime end = parseDateTime(endTime);

            // Create event
            Event event = new Event()
                    .setSummary(summary)
                    .setDescription(description)
                    .setStart(new EventDateTime().setDateTime(start).setTimeZone(timeZone))
                    .setEnd(new EventDateTime().setDateTime(end).setTimeZone(timeZone));

            // Add Google Meet conference data
            ConferenceData conferenceData = new ConferenceData();
            CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
            createConferenceRequest.setRequestId(String.valueOf(System.currentTimeMillis()));
            conferenceData.setCreateRequest(createConferenceRequest);
            event.setConferenceData(conferenceData);

            // Insert the event
            event = service.events().insert("primary", event)
                    .setConferenceDataVersion(1)
                    .execute();

            logger.info("Event created: {}", event.getHtmlLink());
            return event;
        } catch (IOException | GeneralSecurityException e) {
            logger.error("Error creating Google Meet event", e);
            throw new MeetingCreationException("Error al crear el evento de Google Meet: " + e.getMessage(), e);
        } catch (DateTimeParseException e) {
            logger.error("Error parsing date/time", e);
            throw new MeetingCreationException("Formato de fecha/hora inválido: " + e.getMessage(), e);
        }
    }

    /**
     * Parses an ISO-8601 formatted date time string to a Google Calendar DateTime object.
     */
    private DateTime parseDateTime(String dateTimeStr) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(timeZone));
            return new DateTime(zonedDateTime.toInstant().toEpochMilli());
        } catch (DateTimeParseException e) {
            logger.error("Error parsing date: {}", dateTimeStr, e);
            throw e;
        }
    }    /**
     * Creates an authorized Credential object.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets
        InputStream in = GoogleMeetService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
                
        // Usando un puerto diferente (9090) para evitar conflictos con otros procesos
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(9090)
                .build();
                
        // Usar el método que abre automáticamente el navegador
        return new AuthorizationCodeInstalledApp(flow, receiver, new AuthorizationCodeInstalledApp.Browser() {
            @Override
            public void browse(String url) throws IOException {
                logger.info("Abriendo navegador para autenticación de Google...");
                try {
                    // Intenta abrir el navegador predeterminado del sistema
                    if (java.awt.Desktop.isDesktopSupported()) {
                        java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                    } else {
                        // Alternativa en caso de que Desktop no esté soportado
                        abrirNavegadorAlternativo(url);
                    }
                } catch (Exception e) {
                    logger.error("Error al abrir el navegador", e);
                    // Caer en la solución original si falla
                    System.out.println("Por favor, abre este enlace en tu navegador: " + url);
                }
            }
        }).authorize("user");
    }
    
    /**
     * Método alternativo para abrir el navegador en diferentes sistemas operativos
     */
    private void abrirNavegadorAlternativo(String url) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Process process = null;
            
            if (os.contains("win")) {
                // Windows
                process = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                // macOS
                process = Runtime.getRuntime().exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux
                String[] browsers = {"google-chrome", "firefox", "mozilla", "epiphany", "konqueror", "netscape", "opera", "links", "lynx"};
                
                StringBuilder cmd = new StringBuilder();
                for (int i = 0; i < browsers.length; i++) {
                    if (i == 0) {
                        cmd.append(String.format("%s \"%s\"", browsers[i], url));
                    } else {
                        cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
                    }
                }
                
                process = Runtime.getRuntime().exec(new String[]{"sh", "-c", cmd.toString()});
            }
            
            if (process != null) {
                process.waitFor();
            }
        } catch (Exception e) {
            logger.error("Error al abrir navegador alternativo", e);
        }
    }
      /**
     * Lists upcoming events from the primary calendar.
     * Ahora con timeout bajo y logs para evitar bloqueos en el dashboard.
     * @param maxResults Maximum number of events to return
     * @return List of upcoming events
     * @throws IOException If the request fails
     * @throws GeneralSecurityException If there's a security problem
     */
    public List<Event> listUpcomingEvents(int maxResults) throws IOException, GeneralSecurityException {
        logger.info("Solicitando próximos eventos a Google Calendar...");
        // Configurar timeout bajo (2 segundos)
        final int TIMEOUT_MILLIS = 2000;
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // HttpRequestInitializer con timeout
        com.google.api.client.http.HttpRequestInitializer requestInitializer = request -> {
            getCredentials(HTTP_TRANSPORT).initialize(request);
            request.setConnectTimeout(TIMEOUT_MILLIS);
            request.setReadTimeout(TIMEOUT_MILLIS);
        };
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                .setApplicationName(APPLICATION_NAME)
                .build();
        DateTime now = new DateTime(System.currentTimeMillis());
        try {
            Events events = service.events().list("primary")
                    .setMaxResults(maxResults)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            logger.info("Eventos obtenidos correctamente. Total: {}", events.getItems().size());
            return events.getItems();
        } catch (IOException e) {
            logger.error("Error al obtener eventos de Google Calendar: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Gets the calendar embed URL with proper authentication.
     * 
     * @return The URL to the calendar that will display all events
     */
    public String getCalendarEmbedUrl() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
          // Get primary calendar ID
        CalendarList calendarList = service.calendarList().list().execute();
        String primaryCalendarId = "primary";
        
        for (CalendarListEntry calendarListEntry : calendarList.getItems()) {
            Boolean isPrimary = calendarListEntry.isPrimary();
            if (isPrimary != null && isPrimary.booleanValue()) {
                primaryCalendarId = calendarListEntry.getId();
                break;
            }
        }
        
        // URL encode the calendar ID
        String encodedCalendarId = java.net.URLEncoder.encode(primaryCalendarId, "UTF-8");
        
        // Generate calendar embed URL with all needed parameters for a full view
        return "https://calendar.google.com/calendar/embed" +
               "?src=" + encodedCalendarId +
               "&ctz=" + java.net.URLEncoder.encode(timeZone, "UTF-8") +
               "&showTitle=0" +
               "&showNav=1" +
               "&showPrint=0" +
               "&showTabs=1" +
               "&showCalendars=1" +
               "&showTz=1" +
               "&mode=WEEK";
    }
      /**
     * Gets all calendars for the authenticated user.
     * 
     * @return List of CalendarListEntry objects
     */
    public List<CalendarListEntry> getCalendars() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
                
        // Get list of calendars
        CalendarList calendarList = service.calendarList().list().execute();
        return calendarList.getItems();
    }
    
    /**
     * Deletes a Google Calendar event by its ID.
     * 
     * @param eventId The ID of the event to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteEvent(String eventId) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            
            // Delete the event from primary calendar
            service.events().delete("primary", eventId).execute();
            logger.info("Event deleted successfully: {}", eventId);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting event: {}", eventId, e);
            return false;
        }
    }
    
    /**
     * Extracts the event ID from a Google Calendar event URL or Meet link.
     * 
     * @param meetLink The Google Meet link or Calendar event URL
     * @return The event ID or null if not found
     */
    public String extractEventIdFromLink(String meetLink) {
        if (meetLink == null || meetLink.isEmpty()) {
            return null;
        }
        
        try {
            // For Google Meet links
            if (meetLink.contains("meet.google.com")) {
                // Search for events with conferenceData that contains this meet code
                String meetCode = meetLink.substring(meetLink.lastIndexOf("/") + 1);
                
                final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
                
                // Get current date time
                DateTime now = new DateTime(System.currentTimeMillis());
                // Get a date 1 month from now
                DateTime oneMonthFromNow = new DateTime(System.currentTimeMillis() + 30L * 24L * 60L * 60L * 1000L);
                
                // List events
                Events events = service.events().list("primary")
                        .setTimeMin(now)
                        .setTimeMax(oneMonthFromNow)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();
                
                for (Event event : events.getItems()) {
                    if (event.getConferenceData() != null && 
                        event.getConferenceData().getEntryPoints() != null &&
                        !event.getConferenceData().getEntryPoints().isEmpty()) {
                        
                        for (EntryPoint entryPoint : event.getConferenceData().getEntryPoints()) {
                            if (entryPoint.getUri() != null && entryPoint.getUri().contains(meetCode)) {
                                return event.getId();
                            }
                        }
                    }
                }
            }
            
            // For Google Calendar event URLs
            if (meetLink.contains("calendar.google.com")) {
                // Extract the event ID from the URL
                int eidIndex = meetLink.indexOf("eid=");
                if (eidIndex > 0) {
                    String eid = meetLink.substring(eidIndex + 4);
                    int ampIndex = eid.indexOf("&");
                    if (ampIndex > 0) {
                        eid = eid.substring(0, ampIndex);
                    }
                    return eid;
                }
            }
        } catch (Exception e) {
            logger.error("Error extracting event ID from link: {}", meetLink, e);
        }
        
        return null;
    }
    
    /**
     * Extrae la información de una reunión de Google Meet desde la descripción de una tarea.
     * 
     * @param description La descripción de la tarea
     * @return Un objeto MeetingInfo con los datos de la reunión, o null si no hay reunión
     */
    public com.usta.startupconnect.dto.MeetingInfo extractMeetingInfoFromDescription(String description) {
        if (description == null || !description.contains("REUNIÓN DE GOOGLE MEET")) {
            return null;
        }
        
        com.usta.startupconnect.dto.MeetingInfo meetingInfo = new com.usta.startupconnect.dto.MeetingInfo();
        
        try {
            // Extraer el enlace de la reunión
            int startLinkIndex = description.indexOf("Enlace de la reunión: ");
            if (startLinkIndex > 0) {
                startLinkIndex += "Enlace de la reunión: ".length();
                int endLinkIndex = description.indexOf("\n", startLinkIndex);
                if (endLinkIndex < 0) {
                    endLinkIndex = description.length();
                }
                
                String meetLink = description.substring(startLinkIndex, endLinkIndex).trim();
                meetingInfo.setEnlace(meetLink);
                
                // Extraer el ID del evento
                String eventId = this.extractEventIdFromLink(meetLink);
                meetingInfo.setEventId(eventId);
            }
            
            // Extraer la fecha
            int startDateIndex = description.indexOf("Fecha: ");
            if (startDateIndex > 0) {
                startDateIndex += "Fecha: ".length();
                int endDateIndex = description.indexOf(" a las ", startDateIndex);
                if (endDateIndex > 0) {
                    String fechaStr = description.substring(startDateIndex, endDateIndex).trim();
                    meetingInfo.setFecha(fechaStr);
                    
                    // Extraer la hora
                    int startTimeIndex = endDateIndex + " a las ".length();
                    int endTimeIndex = description.indexOf("\n", startTimeIndex);
                    if (endTimeIndex < 0) {
                        endTimeIndex = description.length();
                    }
                    
                    String horaStr = description.substring(startTimeIndex, endTimeIndex).trim();
                    meetingInfo.setHora(horaStr);
                }
            }
            
            // Extraer la duración
            int startDurationIndex = description.indexOf("Duración: ");
            if (startDurationIndex > 0) {
                startDurationIndex += "Duración: ".length();
                int endDurationIndex = description.indexOf(" minutos", startDurationIndex);
                if (endDurationIndex > 0) {
                    String duracionStr = description.substring(startDurationIndex, endDurationIndex).trim();
                    try {
                        meetingInfo.setDuracion(Integer.parseInt(duracionStr));
                    } catch (NumberFormatException e) {
                        // Si hay error, asignar duración por defecto de 30 minutos
                        meetingInfo.setDuracion(30);
                    }
                }
            }
            
            return meetingInfo;
        } catch (Exception e) {
            logger.error("Error al extraer información de reunión de la descripción", e);
            return null;
        }
    }
    
    /**
     * Actualiza un evento existente en Google Calendar.
     * 
     * @param eventId     ID del evento a actualizar
     * @param summary     Nuevo título del evento
     * @param description Nueva descripción del evento
     * @param startTime   Nueva hora de inicio en formato ISO-8601
     * @param endTime     Nueva hora de finalización en formato ISO-8601
     * @return El evento actualizado
     * @throws IOException si hay un error de comunicación
     * @throws GeneralSecurityException si hay un error de seguridad
     */
    public Event updateEvent(String eventId, String summary, String description, String startTime, String endTime) 
            throws IOException, GeneralSecurityException {
        if (eventId == null || eventId.isEmpty()) {
            throw new IllegalArgumentException("El ID del evento no puede estar vacío");
        }
        
        // Construir el servicio de Google Calendar
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
        try {
            // Obtener el evento existente
            Event event = service.events().get("primary", eventId).execute();
            
            if (event == null) {
                throw new IllegalArgumentException("No se encontró el evento con ID: " + eventId);
            }
            
            // Actualizar las propiedades del evento
            event.setSummary(summary);
            event.setDescription(description);
            
            // Actualizar fecha/hora de inicio y fin
            DateTime start = parseDateTime(startTime);
            DateTime end = parseDateTime(endTime);
            
            event.setStart(new EventDateTime().setDateTime(start).setTimeZone(timeZone));
            event.setEnd(new EventDateTime().setDateTime(end).setTimeZone(timeZone));
            
            // Guardar los cambios
            Event updatedEvent = service.events().update("primary", eventId, event).execute();
            logger.info("Evento actualizado: {}", updatedEvent.getHtmlLink());
            
            return updatedEvent;
        } catch (IOException e) {
            logger.error("Error actualizando el evento: {}", eventId, e);
            throw e;
        }
    }
}