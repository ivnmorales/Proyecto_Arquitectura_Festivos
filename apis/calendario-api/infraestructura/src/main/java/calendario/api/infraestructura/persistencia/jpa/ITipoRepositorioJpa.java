package calendario.api.infraestructura.persistencia.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import calendario.api.infraestructura.persistencia.entidades.TipoEntidad;

@Repository
public interface ITipoRepositorioJpa extends JpaRepository<TipoEntidad, Integer> {

    List<TipoEntidad> findAllByOrderByIdAsc();

    @Query("SELECT t FROM TipoEntidad t WHERE LOWER(t.tipo) LIKE LOWER(CONCAT('%', :nombre, '%')) ORDER BY t.id")
    List<TipoEntidad> buscar(@Param("nombre") String nombre);

    // Los Tipo iniciales se insertaron con Id explícito, por eso
    // el siguiente Id se calcula a partir del mayor existente.
    @Query("SELECT COALESCE(MAX(t.id), 0) FROM TipoEntidad t")
    int obtenerMaximoId();
}
