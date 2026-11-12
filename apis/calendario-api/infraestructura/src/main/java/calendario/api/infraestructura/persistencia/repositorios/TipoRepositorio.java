package calendario.api.infraestructura.persistencia.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import calendario.api.core.interfaces.repositorios.ITipoRepositorio;
import calendario.api.dominio.Tipo;
import calendario.api.infraestructura.persistencia.entidades.TipoEntidad;
import calendario.api.infraestructura.persistencia.jpa.ITipoRepositorioJpa;
import calendario.api.infraestructura.persistencia.mapeadores.TipoMapeador;

@Repository
public class TipoRepositorio implements ITipoRepositorio {

    private final ITipoRepositorioJpa tipoRepositorioJpa;

    public TipoRepositorio(ITipoRepositorioJpa tipoRepositorioJpa) {
        this.tipoRepositorioJpa = tipoRepositorioJpa;
    }

    @Override
    public List<Tipo> listar() {
        return tipoRepositorioJpa.findAllByOrderByIdAsc()
                .stream()
                .map(TipoMapeador::aDominio)
                .toList();
    }

    @Override
    public Optional<Tipo> obtenerPorId(int id) {
        return tipoRepositorioJpa.findById(id).map(TipoMapeador::aDominio);
    }

    @Override
    public List<Tipo> buscar(String nombre) {
        return tipoRepositorioJpa.buscar(nombre)
                .stream()
                .map(TipoMapeador::aDominio)
                .toList();
    }

    @Override
    @Transactional
    public Tipo guardar(Tipo tipo) {
        TipoEntidad entidad = TipoMapeador.aEntidad(tipo);

        if (tipo.getId() <= 0) {
            entidad.setId(tipoRepositorioJpa.obtenerMaximoId() + 1);
        }

        return TipoMapeador.aDominio(tipoRepositorioJpa.save(entidad));
    }

    @Override
    @Transactional
    public boolean eliminar(int id) {
        if (!tipoRepositorioJpa.existsById(id)) {
            return false;
        }
        tipoRepositorioJpa.deleteById(id);
        tipoRepositorioJpa.flush();
        return true;
    }
}
