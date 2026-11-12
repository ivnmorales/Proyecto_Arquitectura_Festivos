package calendario.api.core.excepciones;

/**
 * La operación entra en conflicto con los datos existentes
 * (nombre duplicado, tipo requerido o en uso).
 * Se responde con 409.
 */
public class ConflictoExcepcion extends RuntimeException {

    public ConflictoExcepcion(String mensaje) {
        super(mensaje);
    }
}
