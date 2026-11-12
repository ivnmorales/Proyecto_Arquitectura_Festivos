package calendario.api.core.excepciones;

/**
 * El recurso solicitado no existe.
 * Se responde con 404.
 */
public class RecursoNoEncontradoExcepcion extends RuntimeException {

    public RecursoNoEncontradoExcepcion(String mensaje) {
        super(mensaje);
    }
}
