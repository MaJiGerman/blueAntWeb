package es.urjc.etsii.blueantweb.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.urjc.etsii.blueantweb.Entities.Partida;

public interface PartidaRepository extends JpaRepository<Partida, Long>{

	
	@Query(value = "SELECT Id FROM Partida WHERE IdUsuario2 = ?1", nativeQuery = true)
	List<Integer> findByIdUsuario2(int idUsr);

	@Query(value = "SELECT Id,IdUsuario2 FROM Partida WHERE IdUsuario2 IN (?1)", nativeQuery = true)
	List<Object[]> findByIdUsuario2List(List<Integer> idUsrList);
	
}
