package calendario.api.core.interfaces.servicios;

import java.util.List;

import calendario.api.dominio.Calendario;

public interface ICalendarioServicio {

    /**
     * Genera (o regenera) todos los días del año
     * clasificados como laboral, fin de semana o festivo.
     */
    boolean generar(int anio);

    /**
     * Obtiene los días almacenados del año, ordenados por fecha.
     */
    List<Calendario> obtener(int anio);
}
