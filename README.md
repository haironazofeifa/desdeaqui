# Desde Aquí

Plataforma web de tips de viaje enfocada en destinos turísticos de Costa Rica.

---

## Integrantes

- Hairon Daniel Azofeifa Sanchez
  
---

## Tecnologías utilizadas

| Capa | Tecnología |
|------|-----------|
| Backend | Java 21, Spring Boot 3.5.15 |
| Frontend | Thymeleaf, HTML5, CSS3, JavaScript |
| Base de datos | MySQL 8 |
| ORM | Spring Data JPA / Hibernate |

---

## Requisitos previos

- Java 21
- Maven
- MySQL 8

---

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
https://github.com/haironazofeifa/desdeaqui.git
```

### 2. Crear la base de datos

Abrí MySQL Workbench o tu cliente favorito y ejecutá en orden:

```bash
# Primero el schema (crea tablas)
source database/schema.sql

# Luego los datos de prueba
source database/data.sql
```

### 3. Configurar `application.properties`

Editá el archivo `src/main/resources/application.properties` con tus datos:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/desdeaqui
spring.datasource.username=root
spring.datasource.password=TU_CONTRASENA

```

### 4. Ejecutar el proyecto

```bash
./mvnw spring-boot:run
```

Abrí el navegador en: `http://localhost:8081`

---

## Credenciales de prueba

| Rol | Correo | Contraseña |
|-----|--------|-----------|
| Administrador | admin@desdeaqui.com | 1234 |
| Viajero | hairon@correo.com | 1234 |

---

## Arquitectura

El sistema usa una arquitectura **MVC por capas**:

```
controller/   → Recibe peticiones HTTP y devuelve vistas o JSON
service/      → Lógica de negocio
repository/   → Acceso a datos con Spring Data JPA
model/        → Entidades JPA que mapean las tablas
filter/       → Filtro de sesión para proteger rutas
config/       → Configuración de Cloudinary
templates/    → Vistas Thymeleaf (HTML)
static/       → CSS, imágenes, recursos estáticos
```

---

## Estructura del repositorio

```
desdeaqui/
├── src/
│   ├── main/
│   │   ├── java/com/desdeaqui/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── model/
│   │   │   ├── filter/
│   │   │   └── config/
│   │   └── resources/
│   │       ├── templates/
│   │       └── static/
├── database/
│   ├── schema.sql
│   └── data.sql
├── docs/
│   └── capturas/
└── README.md
```

---

## Capturas principales

| Pantalla | Descripción |
|---------|-------------|
| Login | Pantalla de inicio de sesión con estética pixel art |
| Mapa | Mapa interactivo de Costa Rica con pins por destino |
| Explorar | Grid de destinos con filtros por zona, interés y presupuesto |
| Tips | Modal con tips por categoría, estrellas y comentarios |
| Perfil | Estadísticas del viajero y destinos guardados |
| Admin | Panel de gestión de destinos, tips y usuarios |

---

## 🗄️ Base de datos

Los scripts están en la carpeta `database/`:

- `schema.sql` → Crea todas las tablas con sus relaciones
- `data.sql` → Inserta los 5 destinos iniciales y tips de prueba

**Tablas:** `roles`, `usuarios`, `destinos`, `tips`, `guardados`, `puntuaciones_tips`, `comentarios`
