package calendario.api.core.interfaces.integracion;

import java.util.List;

import calendario.api.core.modelos.FestivoExterno;

public interface IFestivosServicioExterno {

    /**
     * Obtiene los festivos calculados por la API Festivos para el año.
     */
    List<FestivoExterno> obtenerFestivos(int anio);
}
