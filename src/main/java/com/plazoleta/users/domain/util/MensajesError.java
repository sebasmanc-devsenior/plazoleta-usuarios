package com.plazoleta.users.domain.util;

/**
 * Mensajes de error del microservicio, en un solo lugar. Se usan en el dominio, la aplicación y la
 * infraestructura (validaciones, excepciones y manejador de errores). Clase de Java puro, sin Spring.
 */
public final class MensajesError {
    public static final String ADMINISTRADOR_INICIAL_NECESITA_NOMBRE_APELLIDO_DOCUMENTO_Y = "El administrador inicial necesita nombre, apellido, documento y una clave de al menos 6 caracteres";
    public static final String APELLIDO_ES_OBLIGATORIO = "El apellido es obligatorio";
    public static final String APELLIDO_PUEDE_TENER_MAXIMO_100_CARACTERES = "El apellido puede tener máximo 100 caracteres";
    public static final String APP_JWT_SECRET_O_USERS_JWT_SECRET = "APP_JWT_SECRET (o USERS_JWT_SECRET) debe tener al menos 32 bytes";
    public static final String AUTENTICACION_ENTRE_SERVICIOS_NO_ESTA_CONFIGURADA = "La autenticación entre servicios no está configurada";
    public static final String CELULAR_DEBE_TENER_MAXIMO_13_CARACTERES = "El celular debe tener máximo 13 caracteres";
    public static final String CELULAR_DEBE_TENER_MAXIMO_13_CARACTERES_Y = "El celular debe tener máximo 13 caracteres y puede empezar con +";
    public static final String CLAVE_DEBE_TENER_ENTRE_6_Y_72 = "La clave debe tener entre 6 y 72 caracteres";
    public static final String CLAVE_ES_OBLIGATORIA = "La clave es obligatoria";
    public static final String CLAVE_PUEDE_TENER_MAXIMO_72_CARACTERES = "La clave puede tener máximo 72 caracteres";
    public static final String CONFIGURA_ADMIN_BOOTSTRAP_EMAIL_Y_ADMIN_BOOTSTRAP = "Configura ADMIN_BOOTSTRAP_EMAIL y ADMIN_BOOTSTRAP_PASSWORD juntos, o ninguno";
    public static final String CORREO_ES_OBLIGATORIO = "El correo es obligatorio";
    public static final String CORREO_O_CLAVE_INCORRECTOS = "Correo o clave incorrectos";
    public static final String CORREO_O_EL_DOCUMENTO_DE_IDENTIDAD_YA = "El correo o el documento de identidad ya están registrados";
    public static final String CORREO_PUEDE_TENER_MAXIMO_254_CARACTERES = "El correo puede tener máximo 254 caracteres";
    public static final String CORREO_YA_ESTA_REGISTRADO = "El correo ya está registrado";
    public static final String CREDENCIALES_DE_SERVICIO_INVALIDAS = "Credenciales de servicio inválidas";
    public static final String CUENTAS_DE_EMPLEADO_NECESITAN_UN_RESTAURANTID_POSITIVO = "Las cuentas de EMPLEADO necesitan un restaurantId positivo";
    public static final String CUERPO_DE_LA_PETICION_ES_OBLIGATORIO = "El cuerpo de la petición es obligatorio";
    public static final String DOCUMENTO_DE_IDENTIDAD_ES_OBLIGATORIO = "El documento de identidad es obligatorio";
    public static final String DOCUMENTO_DE_IDENTIDAD_PUEDE_TENER_MAXIMO_40 = "El documento de identidad puede tener máximo 40 caracteres";
    public static final String DOCUMENTO_DE_IDENTIDAD_SOLO_PUEDE_TENER_DIGITOS = "El documento de identidad solo puede tener dígitos";
    public static final String DOCUMENTO_DE_IDENTIDAD_YA_ESTA_REGISTRADO = "El documento de identidad ya está registrado";
    public static final String FECHA_DE_NACIMIENTO_NO_PUEDE_SER_FUTURA = "La fecha de nacimiento no puede ser futura";
    public static final String FORMATO_DEL_CELULAR_NO_ES_VALIDO = "El formato del celular no es válido";
    public static final String FORMATO_DEL_CORREO_NO_ES_VALIDO = "El formato del correo no es válido";
    public static final String NO_TIENES_PERMISOS_PARA_ESTA_OPERACION = "No tienes permisos para esta operación";
    public static final String NOMBRE_ES_OBLIGATORIO = "El nombre es obligatorio";
    public static final String NOMBRE_PUEDE_TENER_MAXIMO_100_CARACTERES = "El nombre puede tener máximo 100 caracteres";
    public static final String PETICION_NO_ES_VALIDA = "La petición no es válida";
    public static final String PETICION_TIENE_DATOS_INVALIDOS = "La petición tiene datos inválidos";
    public static final String REQUIERE_AUTENTICACION = "Se requiere autenticación";
    public static final String RESTAURANTE_NO_EXISTE = "El restaurante no existe";
    public static final String RESTAURANTID_DEBE_SER_POSITIVO = "El restaurantId debe ser positivo";
    public static final String RESTAURANTID_NO_SE_PERMITE_PARA_ESTE_TIPO = "restaurantId no se permite para este tipo de cuenta";
    public static final String RESTAURANTID_SOLO_SE_USA_EN_CUENTAS_DE = "restaurantId solo se usa en cuentas de EMPLEADO";
    public static final String ROL_ES_OBLIGATORIO = "El rol es obligatorio";
    public static final String SERVICIO_DE_RESTAURANTES_NO_ESTA_DISPONIBLE = "El servicio de restaurantes no está disponible";
    public static final String SERVICIO_DE_RESTAURANTES_RECHAZO_LA_CONSULTA_HTTP = "El servicio de restaurantes rechazó la consulta (HTTP ";
    public static final String SOLO_EL_PROPIETARIO_DEL_RESTAURANTE_PUEDE_CREAR = "Solo el propietario del restaurante puede crear sus empleados";
    public static final String USERS_JWT_EXPIRATION_DEBE_SER_UNA_DURACION = "USERS_JWT_EXPIRATION debe ser una duración positiva";
    public static final String USUARIO_DEBE_SER_MAYOR_DE_EDAD_18 = "El usuario debe ser mayor de edad (18 años o más)";
    public static final String USUARIO_NO_EXISTE = "El usuario no existe";
    public static final String ERROR_INTERNO_DEL_SERVIDOR = "Error interno del servidor";
    public static final String RUTA_NO_EXISTE = "La ruta solicitada no existe";
    public static final String PETICION_NO_SOPORTADA = "La petición no se puede procesar con ese método o formato";

    private MensajesError() {
    }
}
