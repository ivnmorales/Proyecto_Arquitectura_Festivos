package calendario.api.core.excepciones;

/**
 * Datos de entrada inválidos (por ejemplo, un año fuera de rango).
 * Se responde con 400.
 */
public class SolicitudInvalidaExcepcion extends RuntimeException {

    public SolicitudInvalidaExcepcion(String mensaje) {
        super(mensaje);
    }
}
