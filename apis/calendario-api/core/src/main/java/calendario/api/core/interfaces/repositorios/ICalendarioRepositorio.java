package calendario.api.core.interfaces.repositorios;

import java.time.LocalDate;
import java.util.List;

import calendario.api.dominio.Calendario;

public interface ICalendarioRepositorio {

    List<Calendario> obtenerPorRango(LocalDate desde, LocalDate hasta);

    /**
     * Elimina los días del rango y guarda los nuevos
     * en una sola transacción.
     */
    void reemplazarRango(LocalDate desde, LocalDate hasta, List<Calendario> calendarios);
}
