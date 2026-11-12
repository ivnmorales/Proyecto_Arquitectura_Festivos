package calendario.api.core.excepciones;

/**
 * Falla al consumir la API Festivos.
 * Se responde con 502.
 */
public class ServicioExternoExcepcion extends RuntimeException {

    public ServicioExternoExcepcion(String mensaje) {
        super(mensaje);
    }

    public ServicioExternoExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
