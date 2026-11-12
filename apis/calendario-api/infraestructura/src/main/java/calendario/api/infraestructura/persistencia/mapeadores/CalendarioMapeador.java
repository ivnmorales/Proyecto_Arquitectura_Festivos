package calendario.api.infraestructura.persistencia.mapeadores;

import calendario.api.dominio.Calendario;
import calendario.api.infraestructura.persistencia.entidades.CalendarioEntidad;
import calendario.api.infraestructura.persistencia.entidades.TipoEntidad;

public final class CalendarioMapeador {

    private CalendarioMapeador() {
    }

    public static Calendario aDominio(CalendarioEntidad entidad) {
        if (entidad == null) {
            return null;
        }
        return new Calendario(
                entidad.getId(),
                entidad.getFecha(),
                TipoMapeador.aDominio(entidad.getTipo()),
                entidad.getDescripcion());
    }

    /**
     * Recibe la entidad del Tipo ya administrada por JPA
     * para asociarla sin volver a insertarla.
     */
    public static CalendarioEntidad aEntidad(Calendario calendario, TipoEntidad tipo) {
        if (calendario == null) {
            return null;
        }
        CalendarioEntidad entidad = new CalendarioEntidad();
        entidad.setId(calendario.getId());
        entidad.setFecha(calendario.getFecha());
        entidad.setTipo(tipo);
        entidad.setDescripcion(calendario.getDescripcion());
        return entidad;
    }
}
