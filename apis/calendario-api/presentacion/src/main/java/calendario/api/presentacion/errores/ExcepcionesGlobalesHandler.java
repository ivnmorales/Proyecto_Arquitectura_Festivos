package calendario.api.presentacion.errores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import calendario.api.core.excepciones.ConflictoExcepcion;
import calendario.api.core.excepciones.RecursoNoEncontradoExcepcion;
import calendario.api.core.excepciones.ServicioExternoExcepcion;
import calendario.api.core.excepciones.SolicitudInvalidaExcepcion;

@RestControllerAdvice
public class ExcepcionesGlobalesHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExcepcionesGlobalesHandler.class);

    // 400 - Año fuera de rango o datos de Tipo inválidos
    @ExceptionHandler(SolicitudInvalidaExcepcion.class)
    public ResponseEntity<ErrorRespuesta> solicitudInvalida(SolicitudInvalidaExcepcion e) {
        return responder(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // 400 - Cuerpo JSON ausente o mal formado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> cuerpoInvalido(HttpMessageNotReadableException e) {
        return responder(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es un JSON válido");
    }

    // 404 - Tipo inexistente
    @ExceptionHandler(RecursoNoEncontradoExcepcion.class)
    public ResponseEntity<ErrorRespuesta> noEncontrado(RecursoNoEncontradoExcepcion e) {
        return responder(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 409 - Nombre duplicado o Tipo requerido
    @ExceptionHandler(ConflictoExcepcion.class)
    public ResponseEntity<ErrorRespuesta> conflicto(ConflictoExcepcion e) {
        return responder(HttpStatus.CONFLICT, e.getMessage());
    }

    // 409 - Restricción de la base de datos (índice único, Tipo usado en Calendario)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespuesta> integridad(DataIntegrityViolationException e) {
        LOG.warn("Restricción de integridad", e);
        return responder(HttpStatus.CONFLICT,
                "La operación viola una restricción de la base de datos (registro duplicado o en uso)");
    }

    // 400 - Año que no es un número (ej. /obtener/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuesta> parametroInvalido(MethodArgumentTypeMismatchException e) {
        return responder(HttpStatus.BAD_REQUEST,
                "El parámetro '" + e.getName() + "' no es válido: " + e.getValue());
    }

    // 502 - Falla consumiendo la API Festivos
    @ExceptionHandler(ServicioExternoExcepcion.class)
    public ResponseEntity<ErrorRespuesta> servicioExterno(ServicioExternoExcepcion e) {
        LOG.error("Error consumiendo la API Festivos", e);
        return responder(HttpStatus.BAD_GATEWAY, e.getMessage());
    }

    // 500 - Falta un Tipo requerido en PostgreSQL
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorRespuesta> configuracionInvalida(IllegalStateException e) {
        LOG.error("Error de configuración", e);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    // 500 - Cualquier otro error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> errorInesperado(Exception e) {
        // Errores propios de Spring MVC (404 ruta inexistente, 405 método no permitido...)
        if (e instanceof ErrorResponse errorSpring) {
            HttpStatus estado = HttpStatus.valueOf(errorSpring.getStatusCode().value());
            return responder(estado, e.getMessage());
        }

        LOG.error("Error inesperado", e);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado");
    }

    private ResponseEntity<ErrorRespuesta> responder(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado)
                .body(new ErrorRespuesta(estado.value(), estado.getReasonPhrase(), mensaje));
    }
}
