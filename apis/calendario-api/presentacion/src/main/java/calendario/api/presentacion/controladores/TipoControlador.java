package calendario.api.presentacion.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import calendario.api.core.interfaces.servicios.ITipoServicio;
import calendario.api.dominio.Tipo;
import calendario.api.presentacion.errores.ErrorRespuesta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tipos")
@Tag(name = "Tipos", description = "CRUD de los tipos de día (Día laboral, Fin de Semana, Día festivo...)")
public class TipoControlador {

    private final ITipoServicio tipoServicio;

    public TipoControlador(ITipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar los tipos", description = "Devuelve todos los tipos ordenados por Id.")
    @ApiResponse(responseCode = "200", description = "Lista de tipos")
    public ResponseEntity<List<Tipo>> listar() {
        return ResponseEntity.ok(tipoServicio.listar());
    }

    @GetMapping("/obtener/{id}")
    @Operation(summary = "Obtener un tipo por Id")
    @ApiResponse(responseCode = "200", description = "Tipo encontrado")
    @ApiResponse(responseCode = "404", description = "El tipo no existe",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    public ResponseEntity<Tipo> obtener(
            @Parameter(description = "Id del tipo", example = "1") @PathVariable int id) {
        return ResponseEntity.ok(tipoServicio.obtener(id));
    }

    @GetMapping("/buscar/{nombre}")
    @Operation(summary = "Buscar tipos por nombre",
            description = "Devuelve los tipos cuyo nombre contiene el texto, sin distinguir mayúsculas.")
    @ApiResponse(responseCode = "200", description = "Tipos encontrados (puede ser una lista vacía)")
    public ResponseEntity<List<Tipo>> buscar(
            @Parameter(description = "Texto a buscar", example = "festivo") @PathVariable String nombre) {
        return ResponseEntity.ok(tipoServicio.buscar(nombre));
    }

    @PostMapping("/agregar")
    @Operation(summary = "Agregar un tipo",
            description = "Crea un tipo nuevo. El Id lo asigna la API; solo se usa el campo 'tipo'.")
    @ApiResponse(responseCode = "201", description = "Tipo creado")
    @ApiResponse(responseCode = "400", description = "Nombre vacío o demasiado largo",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "409", description = "Ya existe un tipo con ese nombre",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    public ResponseEntity<Tipo> agregar(@RequestBody Tipo tipo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoServicio.agregar(tipo));
    }

    @PutMapping("/modificar")
    @Operation(summary = "Modificar un tipo", description = "Cambia el nombre del tipo indicado por 'id'.")
    @ApiResponse(responseCode = "200", description = "Tipo modificado")
    @ApiResponse(responseCode = "400", description = "Id o nombre inválido",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "404", description = "El tipo no existe",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "409", description = "Ya existe otro tipo con ese nombre",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    public ResponseEntity<Tipo> modificar(@RequestBody Tipo tipo) {
        return ResponseEntity.ok(tipoServicio.modificar(tipo));
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar un tipo",
            description = "Los tipos 1, 2 y 3 los usa la generación del calendario y no se pueden eliminar.")
    @ApiResponse(responseCode = "200", description = "Tipo eliminado",
            content = @Content(schema = @Schema(implementation = Boolean.class, example = "true")))
    @ApiResponse(responseCode = "404", description = "El tipo no existe",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "409", description = "Tipo requerido o en uso",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    public ResponseEntity<Boolean> eliminar(
            @Parameter(description = "Id del tipo", example = "4") @PathVariable int id) {
        return ResponseEntity.ok(tipoServicio.eliminar(id));
    }
}
