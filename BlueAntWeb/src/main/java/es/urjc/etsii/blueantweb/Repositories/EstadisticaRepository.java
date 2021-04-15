package es.urjc.etsii.blueantweb.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.urjc.etsii.blueantweb.Entities.Estadistica;
public interface EstadisticaRepository extends JpaRepository<Estadistica, Long>{
	
	Estadistica findById(long id);
	
	@Query(value = "SELECT Tiempo2 FROM Estadistica WHERE IdPartida = ?1 AND PasosDados > 0", nativeQuery = true)
	List<String> findByIdPartida(int idPartida);
}
