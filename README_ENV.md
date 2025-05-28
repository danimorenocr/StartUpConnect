# StartUpConnect

## Configuración del Entorno

### Requisitos previos
- Java 17 o superior
- Maven
- PostgreSQL

### Configuración de variables de entorno
1. Crea un archivo `.env` en la raíz del proyecto con las siguientes variables:
```
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/db_startup?useSSL=true
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contraseña

# Google Calendar API Settings
GOOGLE_CALENDAR_TIMEZONE=America/Bogota

# OpenAI API Credentials
OPENAI_API_KEY=tu_clave_api_openai

# Render API Settings
RENDER_API_KEY=tu_clave_api_render
RENDER_API_URL=https://api.render.com/v1
```

2. Asegúrate de tener un archivo `credentials.json` en `src/main/resources/` con tus credenciales de Google Calendar API.

### Ejecución del proyecto
1. Instala las dependencias:
```
mvn clean install
```

2. Ejecuta la aplicación:
```
mvn spring-boot:run
```

## Probar la integración de Google Meet

### Endpoints disponibles
- `GET /api/meet/test` - Verifica que la integración esté configurada correctamente
- `GET /api/meet/test-create` - Crea una reunión de prueba automática
- `POST /api/meet/create` - Crea una reunión personalizada con los parámetros especificados

### Ejemplo de uso con curl
```bash
# Probar endpoint básico
curl http://localhost:8080/api/meet/test

# Crear reunión de prueba
curl http://localhost:8080/api/meet/test-create

# Crear reunión personalizada
curl -X POST "http://localhost:8080/api/meet/create?summary=Reunión de Prueba&description=Esta es una reunión de prueba&start=2025-05-29T14:00:00&end=2025-05-29T15:00:00"
```

## Base de datos
```sql
-- Estructura de la base de datos (se incluye en el archivo original README.md)
```
