package es.urjc.etsii.blueantweb.Repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.urjc.etsii.blueantweb.Entities.Usuario;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long>{
	
	Usuario findById(long id);

	@Query(value = "SELECT * FROM Usuario WHERE Edadmeses BETWEEN ?1 AND ?2 AND Nombre IS NOT NULL", nativeQuery = true)
	List<Usuario> findByAge(int min, int max);

	@Query(value = "SELECT * FROM Usuario WHERE Genero = ?1 AND Nombre IS NOT NULL;", nativeQuery = true)
	List<Usuario> findByGender(int gender);
	
	@Query(value = "SELECT * FROM Usuario WHERE Edadmeses BETWEEN ?1 AND ?2 AND Genero = ?3 AND Nombre IS NOT NULL", nativeQuery = true)
	List<Usuario> findByAgeAndGender(int min, int max, int gender);
}
