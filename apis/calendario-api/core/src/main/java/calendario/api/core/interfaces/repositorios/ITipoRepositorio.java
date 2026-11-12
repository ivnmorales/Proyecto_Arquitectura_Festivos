package calendario.api.core.interfaces.repositorios;

import java.util.List;
import java.util.Optional;

import calendario.api.dominio.Tipo;

public interface ITipoRepositorio {

    List<Tipo> listar();

    Optional<Tipo> obtenerPorId(int id);

    /**
     * Tipos cuyo nombre contiene el texto (sin distinguir mayúsculas).
     */
    List<Tipo> buscar(String nombre);

    /**
     * Inserta el tipo si su id es 0, o lo actualiza si ya existe.
     */
    Tipo guardar(Tipo tipo);

    boolean eliminar(int id);
}
