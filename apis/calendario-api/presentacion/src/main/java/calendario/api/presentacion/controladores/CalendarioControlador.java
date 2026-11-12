package calendario.api.presentacion.controladores;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import calendario.api.core.interfaces.servicios.ICalendarioServicio;
import calendario.api.dominio.Calendario;
import calendario.api.presentacion.errores.ErrorRespuesta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/calendario")
@Tag(name = "Calendario", description = "Generación y consulta del calendario laboral")
public class CalendarioControlador {

    private final ICalendarioServicio calendarioServicio;

    public CalendarioControlador(ICalendarioServicio calendarioServicio) {
        this.calendarioServicio = calendarioServicio;
    }

    @PostMapping("/generar/{anio}")
    @Operation(
            summary = "Generar el calendario de un año",
            description = "Consulta los festivos del año en la API Festivos, clasifica cada día como "
                    + "Día festivo, Fin de Semana o Día laboral y lo guarda en PostgreSQL. "
                    + "Si el año ya existe se reemplaza, sin duplicar fechas.")
    @ApiResponse(responseCode = "200", description = "Calendario generado",
            content = @Content(schema = @Schema(implementation = Boolean.class, example = "true")))
    @ApiResponse(responseCode = "400", description = "Año inválido",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "502", description = "Error consumiendo la API Festivos",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "500", description = "Error interno",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    public ResponseEntity<Boolean> generar(
            @Parameter(description = "Año a generar", example = "2026") @PathVariable int anio) {
        return ResponseEntity.ok(calendarioServicio.generar(anio));
    }

    @GetMapping("/obtener/{anio}")
    @Operation(
            summary = "Obtener el calendario de un año",
            description = "Devuelve los días del año almacenados en PostgreSQL, ordenados por fecha.")
    @ApiResponse(responseCode = "200", description = "Días del año")
    @ApiResponse(responseCode = "400", description = "Año inválido",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "500", description = "Error interno",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    public ResponseEntity<List<Calendario>> obtener(
            @Parameter(description = "Año a consultar", example = "2026") @PathVariable int anio) {
        return ResponseEntity.ok(calendarioServicio.obtener(anio));
    }
}
