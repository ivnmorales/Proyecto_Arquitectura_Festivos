package calendario.api.aplicacion.servicios;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import calendario.api.core.excepciones.SolicitudInvalidaExcepcion;
import calendario.api.core.interfaces.integracion.IFestivosServicioExterno;
import calendario.api.core.interfaces.repositorios.ICalendarioRepositorio;
import calendario.api.core.interfaces.repositorios.ITipoRepositorio;
import calendario.api.core.interfaces.servicios.ICalendarioServicio;
import calendario.api.core.modelos.FestivoExterno;
import calendario.api.dominio.Calendario;
import calendario.api.dominio.Tipo;

@Service
public class CalendarioServicio implements ICalendarioServicio {

    // Años positivos de 4 dígitos (formato de fecha AAAA-MM-DD)
    private static final int ANIO_MINIMO = 1;
    private static final int ANIO_MAXIMO = 9999;

    // Tamaño de la columna Calendario.Descripcion
    private static final int LONGITUD_MAXIMA_DESCRIPCION = 100;
    private static final String SEPARADOR_FESTIVOS = " / ";

    private final ICalendarioRepositorio calendarioRepositorio;
    private final ITipoRepositorio tipoRepositorio;
    private final IFestivosServicioExterno festivosServicioExterno;

    public CalendarioServicio(
            ICalendarioRepositorio calendarioRepositorio,
            ITipoRepositorio tipoRepositorio,
            IFestivosServicioExterno festivosServicioExterno) {
        this.calendarioRepositorio = calendarioRepositorio;
        this.tipoRepositorio = tipoRepositorio;
        this.festivosServicioExterno = festivosServicioExterno;
    }

    @Override
    public boolean generar(int anio) {
        validarAnio(anio);

        Tipo diaLaboral = obtenerTipo(Tipo.DIA_LABORAL);
        Tipo finDeSemana = obtenerTipo(Tipo.FIN_DE_SEMANA);
        Tipo diaFestivo = obtenerTipo(Tipo.DIA_FESTIVO);

        // 1. Consultar los festivos antes de tocar la base de datos
        Map<LocalDate, String> festivos = agruparFestivos(
                festivosServicioExterno.obtenerFestivos(anio), anio);

        // 2. Construir el año completo
        LocalDate desde = LocalDate.of(anio, 1, 1);
        LocalDate hasta = LocalDate.of(anio, 12, 31);
        List<Calendario> calendarios = new ArrayList<>();

        for (LocalDate fecha = desde; !fecha.isAfter(hasta); fecha = fecha.plusDays(1)) {
            if (festivos.containsKey(fecha)) {
                calendarios.add(new Calendario(null, fecha, diaFestivo, festivos.get(fecha)));
            } else if (esFinDeSemana(fecha)) {
                calendarios.add(new Calendario(null, fecha, finDeSemana, null));
            } else {
                calendarios.add(new Calendario(null, fecha, diaLaboral, null));
            }
        }

        // 3. Reemplazar el año en una transacción (idempotente)
        calendarioRepositorio.reemplazarRango(desde, hasta, calendarios);

        return true;
    }

    @Override
    public List<Calendario> obtener(int anio) {
        validarAnio(anio);

        return calendarioRepositorio.obtenerPorRango(
                LocalDate.of(anio, 1, 1),
                LocalDate.of(anio, 12, 31));
    }

    private void validarAnio(int anio) {
        if (anio < ANIO_MINIMO || anio > ANIO_MAXIMO) {
            throw new SolicitudInvalidaExcepcion(
                    "El año debe estar entre " + ANIO_MINIMO + " y " + ANIO_MAXIMO);
        }
    }

    private Tipo obtenerTipo(int id) {
        return tipoRepositorio.obtenerPorId(id)
                .orElseThrow(() -> new IllegalStateException(
                        "No existe el Tipo con Id " + id + " en la base de datos"));
    }

    private boolean esFinDeSemana(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }

    /**
     * Agrupa los festivos por fecha. Si varios festivos caen
     * el mismo día se conserva un solo día con todos los nombres.
     */
    private Map<LocalDate, String> agruparFestivos(List<FestivoExterno> festivos, int anio) {
        Map<LocalDate, Set<String>> nombresPorFecha = new LinkedHashMap<>();

        for (FestivoExterno festivo : festivos) {
            if (festivo.getFecha() == null || festivo.getFecha().getYear() != anio) {
                continue;
            }

            nombresPorFecha
                    .computeIfAbsent(festivo.getFecha(), fecha -> new LinkedHashSet<>())
                    .add(festivo.getNombre() == null ? "Festivo" : festivo.getNombre().trim());
        }

        Map<LocalDate, String> descripciones = new LinkedHashMap<>();
        nombresPorFecha.forEach((fecha, nombres) ->
                descripciones.put(fecha, recortar(String.join(SEPARADOR_FESTIVOS, nombres))));

        return descripciones;
    }

    private String recortar(String descripcion) {
        if (descripcion.length() <= LONGITUD_MAXIMA_DESCRIPCION) {
            return descripcion;
        }
        return descripcion.substring(0, LONGITUD_MAXIMA_DESCRIPCION - 3) + "...";
    }
}
