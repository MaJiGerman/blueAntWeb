package es.urjc.etsii.blueantweb.Repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.urjc.etsii.blueantweb.Entities.Usuario;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long>{
	
	Usuario findById(long id);

	@Query(value = "SELECT * FROM Usuario WHERE Edadmeses BETWEEN ?1 AND ?2 AND Exp LIKE ?3 AND Nombre IS NOT NULL ORDER BY Edadmeses ASC", nativeQuery = true)
	List<Usuario> findByAge(int min, int max, String exp);

	@Query(value = "SELECT * FROM Usuario WHERE Edadmeses BETWEEN ?1 AND ?2 AND Exp LIKE ?3 AND Nombre IS NOT NULL AND Centro <> 'NA' AND Centro IS NOT NULL ORDER BY Edadmeses ASC", nativeQuery = true)
	List<Usuario> findByAgeAndCentre(int min, int max, String exp);

	@Query(value = "SELECT * FROM Usuario WHERE Genero = ?1 AND Exp LIKE ?2 AND Nombre IS NOT NULL ORDER BY Edadmeses ASC", nativeQuery = true)
	List<Usuario> findByGender(int gender, String exp);

	@Query(value = "SELECT * FROM Usuario WHERE Genero = ?1 AND Exp LIKE ?2 AND Nombre IS NOT NULL AND Centro <> 'NA' AND Centro IS NOT NULL ORDER BY Edadmeses ASC", nativeQuery = true)
	List<Usuario> findByGenderAndCentre(int gender, String exp);
	
	@Query(value = "SELECT * FROM Usuario WHERE Edadmeses BETWEEN ?1 AND ?2 AND Genero = ?3 AND Exp LIKE ?4 AND Nombre IS NOT NULL ORDER BY Edadmeses ASC", nativeQuery = true)
	List<Usuario> findByAgeAndGender(int min, int max, int gender, String exp);
	
	@Query(value = "SELECT * FROM Usuario WHERE Edadmeses BETWEEN ?1 AND ?2 AND Genero = ?3 AND Exp LIKE ?4 AND Nombre IS NOT NULL AND Centro <> 'NA' AND Centro IS NOT NULL ORDER BY Edadmeses ASC", nativeQuery = true)
	List<Usuario> findByAgeAndGenderAndCentre(int min, int max, int gender, String exp);

	@Query(value = "SELECT DISTINCT Exp FROM Usuario WHERE Exp IS NOT NULL ORDER BY Exp ASC", nativeQuery = true)
	List<Integer> findDistincExp();
}
