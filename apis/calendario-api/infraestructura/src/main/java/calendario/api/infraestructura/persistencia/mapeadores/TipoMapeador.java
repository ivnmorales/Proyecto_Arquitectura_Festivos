package calendario.api.infraestructura.persistencia.mapeadores;

import calendario.api.dominio.Tipo;
import calendario.api.infraestructura.persistencia.entidades.TipoEntidad;

public final class TipoMapeador {

    private TipoMapeador() {
    }

    public static Tipo aDominio(TipoEntidad entidad) {
        if (entidad == null) {
            return null;
        }
        // Algunos registros de Tipo tienen espacios al final
        String tipo = entidad.getTipo() == null ? null : entidad.getTipo().trim();
        return new Tipo(entidad.getId(), tipo);
    }

    public static TipoEntidad aEntidad(Tipo tipo) {
        if (tipo == null) {
            return null;
        }
        TipoEntidad entidad = new TipoEntidad();
        entidad.setId(tipo.getId());
        entidad.setTipo(tipo.getTipo());
        return entidad;
    }
}
