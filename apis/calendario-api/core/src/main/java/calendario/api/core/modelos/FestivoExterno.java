package calendario.api.core.modelos;

import java.time.LocalDate;

/**
 * Festivo obtenido de la API Festivos, independiente
 * del formato de la respuesta HTTP.
 */
public class FestivoExterno {

    private final String nombre;
    private final LocalDate fecha;

    public FestivoExterno(String nombre, LocalDate fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
