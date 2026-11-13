# YJRC_act4_t4 - API REST con Spring Boot, Spring Security y JWT

**Alumno:** Youri Ruiz Chavez
**Materia:** Programacion Web
**Actividad:** A4. Act4. API REST con Spring Boot, Spring Security y JWT (sin frontend)

API REST pura (siempre devuelve JSON, sin vistas) con autenticacion mediante Spring Security y tokens JWT. CRUD de **productos** con su **categoria** (relacion JPA), DTOs, validacion con `@Valid`, paginacion y codigos de estado HTTP apropiados. Probada y documentada con Bruno (coleccion en la carpeta `bruno/`).

## Tecnologias

- Java 21, Spring Boot 4.1.2 (Maven)
- Spring Web, Spring Security, Spring Data JPA (Hibernate), Bean Validation
- MySQL
- JWT con la libreria jjwt 0.12.6
- Bruno

## Como funciona la autenticacion

1. `POST /api/auth/register` guarda el usuario con la contrasena cifrada con **BCrypt**.
2. `POST /api/auth/login` valida email y contrasena y devuelve un **token JWT** firmado (HS256) que vence en 1 hora.
3. Las demas rutas exigen el header `Authorization: Bearer <token>`. El filtro `JwtFiltro` valida la firma y el vencimiento en cada peticion.
4. Sin token, o con un token invalido o vencido, la API responde **401**.

## Endpoints

| Metodo | Ruta | Protegido | Descripcion | Codigos |
|---|---|---|---|---|
| POST | `/api/auth/register` | No | Registrar usuario | 201, 400, 409 |
| POST | `/api/auth/login` | No | Iniciar sesion, devuelve el token | 200, 400, 401 |
| GET | `/api/categorias` | Si | Listar categorias | 200, 401 |
| GET | `/api/productos?page=0&size=5` | Si | Listar productos (paginado) | 200, 400, 401 |
| GET | `/api/productos/{id}` | Si | Ver un producto | 200, 401, 404 |
| POST | `/api/productos` | Si | Crear producto | 201, 400, 401 |
| PUT | `/api/productos/{id}` | Si | Actualizar producto | 200, 400, 401, 404 |
| DELETE | `/api/productos/{id}` | Si | Eliminar producto | 204, 401, 404 |

Ejemplo de cuerpo para crear o actualizar un producto:

```json
{
    "nombre": "Laptop",
    "precio": 12500.5,
    "stock": 10,
    "categoriaId": 1
}
```

Ejemplo de error de validacion (400):

```json
{
    "estado": 400,
    "error": "Bad Request",
    "mensaje": "Datos de entrada invalidos",
    "errores": {
        "nombre": "El nombre es obligatorio",
        "precio": "El precio no puede ser negativo",
        "categoriaId": "La categoriaId es obligatoria"
    }
}
```

## Estructura

```
Peticion -> JwtFiltro (Spring Security) -> Controller -> Service -> Repository -> MySQL
```

- `models`: entidades `Usuario`, `Categoria`, `Producto` (`@ManyToOne` hacia `Categoria`)
- `dto`: `RegisterRequest`, `LoginRequest`, `AuthResponse`, `UsuarioResponse`, `ProductoRequest`, `ProductoResponse`, `CategoriaResponse`, `PaginaResponse`
- `repositorio`: repositorios JPA
- `servicios`: `AuthServicio`, `ProductoServicio`
- `controllers`: `AuthController`, `ProductoController`, `CategoriaController`
- `seguridad`: `SecurityConfig`, `JwtFiltro`, `JwtServicio`, `UsuarioDetallesServicio`
- `excepciones`: manejo centralizado de errores con codigos HTTP
- `config`: `DatosIniciales` (crea 3 categorias si la tabla esta vacia)

El password nunca se expone: las respuestas usan DTOs (`UsuarioResponse` no incluye contrasena).

## Como ejecutarlo en localhost

Requisitos: JDK 21 o superior y MySQL 8 o superior en `localhost:3306`. La base de datos `act4_db`, las tablas y las categorias se crean solas al arrancar.

Variables de entorno (no hay credenciales reales en el repositorio):

| Variable | Descripcion | Valor por defecto |
|---|---|---|
| `DB_HOST` | Servidor de MySQL | `localhost` |
| `DB_NAME` | Base de datos | `act4_db` |
| `DB_USER` | Usuario | `root` |
| `DB_PASSWORD` | Contrasena | (vacia) |
| `JWT_SECRET` | Secreto para firmar los tokens (minimo 32 caracteres) | valor de desarrollo |

```
git clone https://github.com/youri22-eng/YJRC_act4_t4.git
cd YJRC_act4_t4
$env:DB_PASSWORD="tu_password"
.\mvnw.cmd spring-boot:run
```

En Linux o Mac: `export DB_PASSWORD=tu_password` y `./mvnw spring-boot:run`.

La API corre en el puerto **8086**: http://localhost:8086

## Pruebas con Bruno

La coleccion esta en la carpeta `bruno/` (13 peticiones con aserciones del codigo de estado y 2 entornos: `local` y `vps`).

1. Abrir Bruno, **Open Collection** y seleccionar la carpeta `bruno`.
2. Elegir el entorno `local` o `vps`.
3. Ejecutar en orden (la peticion 03 guarda el token y la 07 guarda el id), o usar **Run** sobre la coleccion.

| # | Peticion | Codigo esperado |
|---|---|---|
| 01 | Registro | 201 |
| 02 | Registro con datos invalidos | 400 |
| 03 | Login (guarda el token) | 200 |
| 04 | Login con contrasena incorrecta | 401 |
| 05 | Endpoint protegido sin token | 401 |
| 06 | Listar categorias | 200 |
| 07 | Crear producto | 201 |
| 08 | Crear producto invalido | 400 |
| 09 | Listar productos paginado | 200 |
| 10 | Ver un producto | 200 |
| 11 | Actualizar producto | 200 |
| 12 | Eliminar producto | 204 |
| 13 | Ver producto eliminado | 404 |

## Despliegue en la VPS

La Actividad 4 corre en el puerto **8086**. Las Actividades 1 (8081), 2 (8083) y 3 (8085) siguen corriendo sin cambios.

- URL base de la API: http://194.195.86.95:8086
- Prueba rapida (sin token debe responder 401): http://194.195.86.95:8086/api/productos
- Las credenciales de la base de datos y el secreto JWT de la VPS se pasan por variables de entorno y no estan en el repositorio.