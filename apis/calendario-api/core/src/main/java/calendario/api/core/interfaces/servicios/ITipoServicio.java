package calendario.api.core.interfaces.servicios;

import java.util.List;

import calendario.api.dominio.Tipo;

public interface ITipoServicio {

    List<Tipo> listar();

    Tipo obtener(int id);

    List<Tipo> buscar(String nombre);

    Tipo agregar(Tipo tipo);

    Tipo modificar(Tipo tipo);

    boolean eliminar(int id);
}
