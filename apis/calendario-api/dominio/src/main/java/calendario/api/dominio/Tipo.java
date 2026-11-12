package calendario.api.dominio;

/**
 * Clasificación de un día del calendario:
 * Día laboral, Fin de Semana o Día festivo.
 */
public class Tipo {

    // Ids de los tipos que usa la generación del calendario
    public static final int DIA_LABORAL = 1;
    public static final int FIN_DE_SEMANA = 2;
    public static final int DIA_FESTIVO = 3;

    private int id;
    private String tipo;

    public Tipo() {
    }

    public Tipo(int id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
