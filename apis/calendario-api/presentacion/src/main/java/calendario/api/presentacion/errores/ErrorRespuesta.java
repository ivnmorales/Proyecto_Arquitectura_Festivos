package calendario.api.presentacion.errores;

import java.time.LocalDateTime;

public class ErrorRespuesta {

    private final LocalDateTime fecha;
    private final int estado;
    private final String error;
    private final String mensaje;

    public ErrorRespuesta(int estado, String error, String mensaje) {
        this.fecha = LocalDateTime.now();
        this.estado = estado;
        this.error = error;
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getEstado() {
        return estado;
    }

    public String getError() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }
}
