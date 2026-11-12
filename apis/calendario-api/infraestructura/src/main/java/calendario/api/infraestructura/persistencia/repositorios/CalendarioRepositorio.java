package calendario.api.infraestructura.persistencia.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import calendario.api.core.interfaces.repositorios.ICalendarioRepositorio;
import calendario.api.dominio.Calendario;
import calendario.api.infraestructura.persistencia.entidades.CalendarioEntidad;
import calendario.api.infraestructura.persistencia.jpa.ICalendarioRepositorioJpa;
import calendario.api.infraestructura.persistencia.jpa.ITipoRepositorioJpa;
import calendario.api.infraestructura.persistencia.mapeadores.CalendarioMapeador;

@Repository
public class CalendarioRepositorio implements ICalendarioRepositorio {

    private final ICalendarioRepositorioJpa calendarioRepositorioJpa;
    private final ITipoRepositorioJpa tipoRepositorioJpa;

    public CalendarioRepositorio(
            ICalendarioRepositorioJpa calendarioRepositorioJpa,
            ITipoRepositorioJpa tipoRepositorioJpa) {
        this.calendarioRepositorioJpa = calendarioRepositorioJpa;
        this.tipoRepositorioJpa = tipoRepositorioJpa;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Calendario> obtenerPorRango(LocalDate desde, LocalDate hasta) {
        return calendarioRepositorioJpa.findByFechaBetweenOrderByFechaAsc(desde, hasta)
                .stream()
                .map(CalendarioMapeador::aDominio)
                .toList();
    }

    @Override
    @Transactional
    public void reemplazarRango(LocalDate desde, LocalDate hasta, List<Calendario> calendarios) {
        calendarioRepositorioJpa.eliminarPorRango(desde, hasta);

        List<CalendarioEntidad> entidades = calendarios.stream()
                .map(calendario -> CalendarioMapeador.aEntidad(
                        calendario,
                        tipoRepositorioJpa.getReferenceById(calendario.getTipo().getId())))
                .toList();

        calendarioRepositorioJpa.saveAll(entidades);
    }
}
