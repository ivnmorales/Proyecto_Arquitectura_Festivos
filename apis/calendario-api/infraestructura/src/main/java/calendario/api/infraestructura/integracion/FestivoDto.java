package calendario.api.infraestructura.integracion;

/**
 * Respuesta HTTP de GET /api/festivos/obtener/{anio}:
 * { "nombre": "Navidad", "fecha": "2026-12-25" }
 */
public class FestivoDto {

    private String nombre;
    private String fecha;

    public FestivoDto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
