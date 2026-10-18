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
| 5 | Autenticación: login con JWT y permisos por rol | `historia5-autenticacion` |

## Arquitectura hexagonal (arquetipo del curso)

```
src/main/java/com/plazoleta/users/
├── domain/                     ← núcleo, sin dependencias de frameworks
│   ├── model/                  UserAccount, UserRole, AccountRules (validaciones de la HU-1)
│   └── exception/              AccountException
├── application/                ← casos de uso y puertos, sin Spring
│   ├── port/
│   │   ├── input/              un caso de uso por historia (CreateOwnerUseCase, LoginUseCase…)
│   │   └── output/             UserAccountRepositoryPort, PasswordEncoderPort, TokenProviderPort…
│   ├── service/                implementación de cada caso de uso
│   └── dto/                    datos que entran y salen de los casos de uso
└── infrastructure/             ← adaptadores y frameworks
    ├── input/
    │   └── rest/               controladores REST, dto/ y exceptionhandler/
    ├── output/
    │   ├── persistence/
    │   │   ├── entity/         UserEntity (tabla users)
    │   │   ├── repository/     SpringUserRepository (Spring Data JPA)
    │   │   └── adapter/        UserPersistenceAdapter (implementa el puerto)
    │   ├── security/           BCrypt y JWT
    │   └── http/               consulta al servicio de restaurantes
    └── configuration/          BeanConfiguration (inyección), seguridad, OpenAPI; security/ = filtro JWT
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
