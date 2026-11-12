package calendario.api.aplicacion.servicios;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import calendario.api.core.excepciones.ConflictoExcepcion;
import calendario.api.core.excepciones.RecursoNoEncontradoExcepcion;
import calendario.api.core.excepciones.SolicitudInvalidaExcepcion;
import calendario.api.core.interfaces.repositorios.ITipoRepositorio;
import calendario.api.core.interfaces.servicios.ITipoServicio;
import calendario.api.dominio.Tipo;

@Service
public class TipoServicio implements ITipoServicio {

    // Tamaño de la columna Tipo.Tipo
    private static final int LONGITUD_MAXIMA_TIPO = 100;

    // Tipos que necesita la generación del calendario
    private static final Set<Integer> TIPOS_REQUERIDOS =
            Set.of(Tipo.DIA_LABORAL, Tipo.FIN_DE_SEMANA, Tipo.DIA_FESTIVO);

    private final ITipoRepositorio tipoRepositorio;

    public TipoServicio(ITipoRepositorio tipoRepositorio) {
        this.tipoRepositorio = tipoRepositorio;
    }

    @Override
    public List<Tipo> listar() {
        return tipoRepositorio.listar();
    }

    @Override
    public Tipo obtener(int id) {
        return tipoRepositorio.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion(
                        "No existe el Tipo con Id " + id));
    }

    @Override
    public List<Tipo> buscar(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new SolicitudInvalidaExcepcion("Debe enviar el nombre a buscar");
        }
        return tipoRepositorio.buscar(nombre.trim());
    }

    @Override
    public Tipo agregar(Tipo tipo) {
        String nombre = validarNombre(tipo);
        validarNombreUnico(nombre, 0);

        return tipoRepositorio.guardar(new Tipo(0, nombre));
    }

    @Override
    public Tipo modificar(Tipo tipo) {
        if (tipo == null || tipo.getId() <= 0) {
            throw new SolicitudInvalidaExcepcion("Debe enviar el Id del Tipo a modificar");
        }

        obtener(tipo.getId());
        String nombre = validarNombre(tipo);
        validarNombreUnico(nombre, tipo.getId());

        return tipoRepositorio.guardar(new Tipo(tipo.getId(), nombre));
    }

    @Override
    public boolean eliminar(int id) {
        obtener(id);

        if (TIPOS_REQUERIDOS.contains(id)) {
            throw new ConflictoExcepcion(
                    "El Tipo con Id " + id + " es requerido para generar el calendario y no se puede eliminar");
        }

        return tipoRepositorio.eliminar(id);
    }

    private String validarNombre(Tipo tipo) {
        if (tipo == null || tipo.getTipo() == null || tipo.getTipo().isBlank()) {
            throw new SolicitudInvalidaExcepcion("Debe enviar el nombre del Tipo");
        }

        String nombre = tipo.getTipo().trim();
        if (nombre.length() > LONGITUD_MAXIMA_TIPO) {
            throw new SolicitudInvalidaExcepcion(
                    "El nombre del Tipo no puede superar " + LONGITUD_MAXIMA_TIPO + " caracteres");
        }
        return nombre;
    }

    /**
     * No permite dos tipos con el mismo nombre (sin distinguir mayúsculas).
     */
    private void validarNombreUnico(String nombre, int idActual) {
        boolean duplicado = tipoRepositorio.listar().stream()
                .anyMatch(existente -> existente.getId() != idActual
                        && existente.getTipo() != null
                        && existente.getTipo().trim().equalsIgnoreCase(nombre));

        if (duplicado) {
            throw new ConflictoExcepcion("Ya existe un Tipo con el nombre '" + nombre + "'");
        }
    }
}
