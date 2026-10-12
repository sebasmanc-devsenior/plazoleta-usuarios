# Plazoleta · Microservicio de usuarios

Backend de la plazoleta de comidas: **cuentas, roles y autenticación (JWT)**. Los restaurantes
y platos viven en el microservicio de restaurantes
([plazoleta-restaurantes](https://github.com/sebasmanc-devsenior/plazoleta-restaurantes)).

- Java 25 · Spring Boot 4.1.1 · MySQL 8 · Puerto **8081**
- Arquitectura hexagonal · Pruebas con JUnit 5, Mockito y H2 · Documentación OpenAPI (Swagger)

## Historias de usuario

Cada historia se desarrolló en su propia rama y se unió a `main` en orden.

| HU | Funcionalidad | Rama |
| --- | --- | --- |
| 1 | Crear propietario: validaciones, mayor de edad, clave con BCrypt | `historia1-crear-propietario` |
| 2 | Consulta interna de usuarios (la usa restaurantes para validar al propietario) | `historia2-consulta-de-usuarios` |

## Arquitectura hexagonal

```
src/main/java/com/plazoleta/users/
├── domain/              ← núcleo, sin dependencias de frameworks
│   ├── models/          UserAccount, UserRole
│   ├── rules/           AccountRules (validaciones de la HU-1)
│   └── exceptions/
├── application/         ← casos de uso y puertos, sin Spring
│   ├── dto/
│   ├── ports/input/     un caso de uso por historia (CreateOwnerUseCase, LoginUseCase…)
│   ├── ports/output/    UserAccountRepositoryPort, PasswordEncoderPort, TokenProviderPort…
│   └── services/        implementación de cada caso de uso
└── infrastructure/      ← adaptadores y frameworks
    ├── adapters/input/rest/          controladores REST
    ├── adapters/output/persistence/  JPA + MySQL
    ├── adapters/output/security/     BCrypt y JWT
    ├── adapters/output/http/         consulta al servicio de restaurantes
    ├── config/          BeanConfiguration (inyección), seguridad, OpenAPI
    ├── security/        filtro JWT
    └── exceptionhandler/
```

## Cómo ejecutar

1. **Base de datos:** ejecuta `db/mysql-init.sql` en MySQL Workbench (usuario root).
   Crea la base, el usuario de conexión y el administrador inicial.
2. **Variables de entorno:** copia la plantilla en la raíz del proyecto:
   ```powershell
   Copy-Item .env.example .env
   ```
3. **Levanta el servicio:**
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
4. **Swagger:** http://localhost:8081/swagger-ui.html — haz login con `POST /api/login`
   y pega el token con el botón **Authorize**.

## Pruebas

```powershell
.\mvnw.cmd test
```

Las pruebas usan H2 en memoria: no necesitan MySQL ni tocan la base real.
