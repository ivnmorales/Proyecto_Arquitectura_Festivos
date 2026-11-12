package calendario.api.infraestructura.integracion;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import calendario.api.core.excepciones.ServicioExternoExcepcion;
import calendario.api.core.interfaces.integracion.IFestivosServicioExterno;
import calendario.api.core.modelos.FestivoExterno;

@Service
public class FestivosServicioExterno implements IFestivosServicioExterno {

    private final HttpServicio httpServicio;
    private final String festivosApiUrl;

    public FestivosServicioExterno(
            HttpServicio httpServicio,
            @Value("${festivos.api.url}") String festivosApiUrl) {
        this.httpServicio = httpServicio;
        this.festivosApiUrl = festivosApiUrl;
    }

    @Override
    public List<FestivoExterno> obtenerFestivos(int anio) {
        FestivoDto[] respuesta = httpServicio.get(
                festivosApiUrl + "/obtener/" + anio, FestivoDto[].class);

        if (respuesta == null) {
            throw new ServicioExternoExcepcion("La API Festivos no devolvió datos para el año " + anio);
        }

        return Arrays.stream(respuesta)
                .map(this::aFestivoExterno)
                .toList();
    }

    private FestivoExterno aFestivoExterno(FestivoDto dto) {
        try {
            return new FestivoExterno(dto.getNombre(), LocalDate.parse(dto.getFecha()));
        } catch (DateTimeParseException | NullPointerException e) {
            throw new ServicioExternoExcepcion(
                    "La API Festivos devolvió una fecha inválida: " + dto.getFecha(), e);
        }
    }
}
