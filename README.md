# StartUpConnect

<div align="center">
  <img src="src/main/resources/static/images/logoTexto.png" alt="StartUpConnect Logo" width="400">
  <p><em>Conectando emprendedores con mentores y oportunidades</em></p>
</div>

## 📋 Descripción del Proyecto

StartUpConnect es una plataforma integral diseñada para conectar emprendedores con mentores experimentados, inversionistas y oportunidades de crecimiento. La plataforma facilita la gestión de startups, convocatorias, postulaciones y el proceso de mentoría, creando un ecosistema completo para el desarrollo de emprendimientos.

## 🚀 Características Principales

- **Gestión de Startups**: Registro y administración de proyectos emprendedores con toda la información relevante.
- **Sistema de Convocatorias**: Publicación y gestión de convocatorias para startups con seguimiento completo del proceso.
- **Mentoría Estructurada**: Proceso de mentoría organizado en etapas y tareas específicas.
- **Feedback Continuo**: Sistema de retroalimentación entre mentores y emprendedores.
- **Calendario Integrado**: Integración con Google Calendar para la gestión de eventos y recordatorios.
- **Vitrina de Startups**: Exposición de proyectos para atraer inversionistas y colaboradores.
- **Gestión de Entregables**: Sistema para subir, revisar y aprobar documentos relacionados con las tareas.
- **Chatbot Asistente**: Asistente virtual basado en IA para ayudar a los usuarios con sus consultas.

## 🛠️ Tecnologías Utilizadas

- **Backend**: Java 17 con Spring Boot 3.4.2
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript
- **Base de Datos**: PostgreSQL
- **Integración**: Google Calendar API, OpenAI API
- **Despliegue**: Render Cloud Platform
- **Herramientas de Construcción**: Maven

## 📦 Estructura del Proyecto

```
StartUpConnect/
├── src/
│   ├── main/
│   │   ├── java/com/usta/startupconnect/
│   │   │   ├── controllers/        # Controladores MVC
│   │   │   ├── entities/           # Entidades JPA
│   │   │   ├── models/
│   │   │   │   ├── dao/            # Interfaces DAO para acceso a datos
│   │   │   │   └── services/       # Servicios de la aplicación
│   │   │   └── utils/              # Utilidades y clases auxiliares
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/            # Hojas de estilo
│   │       │   ├── images/         # Imágenes y recursos gráficos
│   │       │   ├── js/             # Scripts JavaScript
│   │       │   └── sounds/         # Recursos de audio
│   │       ├── templates/          # Plantillas Thymeleaf
│   │       └── application.properties  # Configuración de la aplicación
│   └── test/                       # Pruebas unitarias
└── pom.xml                         # Configuración de Maven
```

## 🗄️ Modelo de Base de Datos

La aplicación utiliza un modelo de base de datos relacional con las siguientes entidades principales:

- **Usuarios**: Administradores, mentores, emprendedores e inversionistas
- **Startups**: Proyectos emprendedores con toda su información
- **Convocatorias**: Oportunidades para startups
- **Postulaciones**: Aplicaciones de startups a convocatorias
- **Etapas y Tareas**: Estructura del proceso de mentoría
- **Entregables**: Documentos y archivos asociados a las tareas
- **Eventos y Calendario**: Gestión de fechas importantes

## 🚦 Requisitos Previos

- Java Development Kit (JDK) 17 o superior
- Maven 3.8+
- PostgreSQL 13+
- Cuenta de Google Cloud Platform (para integración con Google Calendar)
- Cuenta en OpenAI (para el chatbot asistente)

## ⚙️ Configuración del Entorno

1. **Base de Datos PostgreSQL**:
   - Crear una base de datos para el proyecto
   - Configurar las credenciales en variables de entorno o application.properties

2. **Variables de Entorno**:
   ```
   DB_URL=jdbc:postgresql://localhost:5432/nombre_db
   DB_USERNAME=usuario_db
   DB_PASSWORD=contraseña_db
   GOOGLE_CALENDAR_TIMEZONE=America/Bogota
   GOOGLE_CALENDAR_CREDENTIALS_PATH=ruta/a/credentials.json
   OPENAI_API_KEY=tu_api_key_de_openai
   RENDER_API_KEY=tu_api_key_de_render
   RENDER_API_URL=https://api.render.com/v1
   ```

3. **Credenciales de Google Calendar**:
   - Configurar un proyecto en Google Cloud Platform
   - Activar la API de Google Calendar
   - Generar credenciales OAuth 2.0
   - Guardar el archivo credentials.json en la ruta especificada

## 🔧 Instalación y Ejecución

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/tuusuario/StartUpConnect.git
   cd StartUpConnect
   ```

2. **Compilar el proyecto**:
   ```bash
   ./mvnw clean package
   ```

3. **Ejecutar la aplicación**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acceder a la aplicación**:
   Abrir un navegador y visitar `http://localhost:8080`

## 📝 Estructura de la Base de Datos

```sql
-- Tabla: Rol
CREATE TABLE Rol (
id SERIAL PRIMARY KEY,
rol VARCHAR(50) NOT NULL
);

-- Tabla: Usuario
CREATE TABLE Usuario (
documento VARCHAR(20) PRIMARY KEY,
nombre VARCHAR(100),
email VARCHAR(100) UNIQUE,
contrasenna TEXT NOT NULL,
fecha_creacion DATE,
telefono VARCHAR(20),
foto_url TEXT,
id_rol INTEGER REFERENCES Rol(id)
);

-- Tabla: Emprendedor
CREATE TABLE Emprendedor (
documento VARCHAR(20) PRIMARY KEY REFERENCES Usuario(documento),
universidad VARCHAR(100),
programa_educativo VARCHAR(100)
);

-- Tabla: Mentor
CREATE TABLE Mentor (
documento VARCHAR(20) PRIMARY KEY REFERENCES Usuario(documento),
especialidad VARCHAR(100),
biografia VARCHAR(250),
linkedin TEXT,
anos_experiencia SMALLINT
);

-- Tabla: Startup
CREATE TABLE Startup (
id SERIAL PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
sector VARCHAR(50),
descripcion TEXT,
pagina_web TEXT,
logo_url TEXT,
pitch_url TEXT,
video_presentacion TEXT,
github TEXT,
linkedin TEXT,
instagram TEXT,
fecha_creacion DATE,
estado VARCHAR(50),
cant_likes SMALLINT,
id_emprendedor VARCHAR(20) REFERENCES Emprendedor(documento)
);
```

### Datos iniciales

```sql
INSERT INTO ROLES (id, rol)
VALUES
(1, 'ADMIN'),
(2, 'MENTOR'),
(3, 'EMPRENDEDOR'),
(4, 'INVERSIONISTA');

INSERT INTO USUARIOS (documento, nombre, email, contrasenna, fecha_creacion, telefono, foto_url, id_rol)
VALUES
('1234567890', 'Maria Rodriguez', 'maria@example.com', '1234', '2025-05-01', '3001234567', 'https://example.com/maria.jpg', 2),
('0987654321', 'Juan Perez', 'juan@example.com', 'abcd', '2025-04-15', '3109876543', 'https://example.com/juan.jpg', 3),
('1122334455', 'Carlos Garcia', 'carlos@example.com', 'qwerty', '2025-03-22', '3201122334', 'https://example.com/carlos.jpg', 3),
('5566778899', 'Ana Lopez', 'ana@example.com', 'pass123', '2025-02-10', '3005566778', 'https://example.com/ana.jpg', 3),
('6677889900', 'Pedro Martinez', 'pedro@example.com', 'pedro123', '2025-01-20', '3106677889', 'https://example.com/pedro.jpg', 2);
```

## 🔐 Seguridad

La aplicación implementa medidas de seguridad que incluyen:
- Autenticación de usuarios con roles específicos
- Protección de rutas según el rol del usuario
- Encriptación de contraseñas
- Manejo seguro de variables de entorno para credenciales

## 🧪 Pruebas

El proyecto incluye pruebas unitarias y de integración para verificar el correcto funcionamiento de los componentes. Para ejecutar las pruebas:

```bash
./mvnw test
```

## 🤝 Contribuir al Proyecto

Las contribuciones son bienvenidas. Para contribuir:

1. Haz un fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/amazing-feature`)
3. Haz commit de tus cambios (`git commit -m 'Add some amazing feature'`)
4. Haz push a la rama (`git push origin feature/amazing-feature`)
5. Abre un Pull Request

## 👥 Equipo de Desarrollo

- **Desarrolladores** - Daniela Moreno - Sofia Torres
- **Supervisores** - Docentes del área de Desarrollo Empresarial

## 📄 Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo LICENSE.md para más detalles.

## 🙏 Agradecimientos

- Universidad Santo Tomás
- Profesores y mentores que apoyaron el desarrollo
- Comunidad de emprendedores que participó en las pruebas


## 🐳 Docker

Esta sección explica cómo desplegar la aplicación utilizando Docker para crear un entorno aislado y reproducible.

### Requisitos previos
- Docker y Docker Compose instalados en tu sistema
- Acceso a terminal con permisos para ejecutar comandos Docker

### Pasos para el despliegue

1. **Navegar al directorio del proyecto**:
   ```bash
   cd C:\Users\danim\OneDrive\Documentos\U\Desarrollo Empresarial\StartUpConnect
   ```

2. **Compilar el proyecto sin ejecutar pruebas**:
   ```bash
   ./mvnw clean package -DskipTests
   ```

3. **Crear la imagen Docker**:
   ```bash
   docker build --no-cache -t startupconnect-app .
   ```

4. **Levantar los contenedores con Docker Compose**:
   ```bash
   docker-compose up -d
   ```

5. **Inicializar la base de datos con datos iniciales**:
   ```bash
   docker exec -it postgres_startup psql -U user_java -d db_startup
   ```
   
   Dentro de PostgreSQL, ejecutar:
   ```sql
   INSERT INTO ROLES (id, rol)
   VALUES
   (1, 'ADMIN'),
   (2, 'MENTOR'),
   (3, 'EMPRENDEDOR'),
   (4, 'INVERSIONISTA');
   
   SELECT * FROM roles;

   INSERT INTO usuarios (documento, nombre, email, "contrasenna", fecha_creacion, telefono, foto_url, id_rol) 
   VALUES (
   '1234567890', 
   'Administrador', 
   'administrador@gmail.com', 
   '$2a$10$4pCHZ3x2Ig1eijTzaQTJfulKo7kb/tduYnq.UKiM0BnIA3KHtPCFi', 
   '2025-05-01', 
   '3001234567', 
   'https://example.com/maria.jpg', 
   1
   );
   ```

### Gestión del contenedor

- **Eliminar el contenedor de la aplicación**:
  ```bash
  docker rm -f springboot_startup_app
  ```

- **Verificar los volúmenes persistentes**:
  ```bash
  docker volume ls
  ```
  Deberías ver un volumen llamado `nombre_proyecto_postgres_data`

### Actualizar la aplicación

Si realizas cambios en el código o la configuración:

1. **Recompilar el proyecto**:
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. **Reconstruir la imagen Docker**:
   ```bash
   docker build --no-cache -t startupconnect-app .
   ```

3. **Reiniciar los contenedores**:
   ```bash
   docker compose up -d
   ```

### Configuración para Docker

Para ejecutar correctamente la aplicación en Docker, es necesario configurar adecuadamente el archivo `application.properties`. A continuación se muestra un ejemplo de configuración para entorno Docker:

```properties
#Conexion a la base de datos (use es para seguridad)
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}


#motor de base de datos
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

#mostrar errores en consultas
spring.jpa.show-sql=true

#Generar mapeo BD
spring.jpa.hibernate.ddl-auto=update

#tamaño de almacenamiento
spring.servlet.multipart.max-file-size=2MB
spring.servlet.multipart.max-request-size=2MB

# Google Calendar API settings
google.calendar.timezone=${GOOGLE_CALENDAR_TIMEZONE:America/Bogota}
google.calendar.credentials.path=${GOOGLE_CALENDAR_CREDENTIALS_PATH:src/main/resources/credentials/credentials.json}

#Creedenciales api de OpenAI
openai.api.key=${OPENAI_API_KEY}

# ApiKey para render
render.api.key=${RENDER_API_KEY}
render.api.url=${RENDER_API_URL:https://api.render.com/v1}

```

> **Nota importante**: Observa que la URL de la base de datos utiliza `postgres_startup` como host, que es el nombre del servicio definido en el archivo `docker-compose.yml`, en lugar de `localhost`.
