package calendario.api.infraestructura.persistencia.jpa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import calendario.api.infraestructura.persistencia.entidades.CalendarioEntidad;

@Repository
public interface ICalendarioRepositorioJpa extends JpaRepository<CalendarioEntidad, Integer> {

    @EntityGraph(attributePaths = "tipo")
    List<CalendarioEntidad> findByFechaBetweenOrderByFechaAsc(LocalDate desde, LocalDate hasta);

    // Borrado masivo: se ejecuta de inmediato, antes de los INSERT,
    // para no violar el índice único sobre Fecha.
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM CalendarioEntidad c WHERE c.fecha BETWEEN :desde AND :hasta")
    int eliminarPorRango(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}
