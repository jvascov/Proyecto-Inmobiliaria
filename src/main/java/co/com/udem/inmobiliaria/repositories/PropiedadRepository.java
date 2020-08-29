package co.com.udem.inmobiliaria.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import co.com.udem.inmobiliaria.entities.Propiedad;

public interface PropiedadRepository extends CrudRepository<Propiedad, Long> {

	 @Query("SELECT u FROM Propiedad u WHERE u.valor = ?1 and u.habitaciones= ?2 and u.area=?3")
	   public List<Propiedad> obtenerPropiedades(Long precio, int numhabitaciones, Long area);

	@Query("SELECT u FROM Propiedad u WHERE u.valor >= ?1 and u.valor <= ?2 "
	+ "and u.habitaciones>= ?3 and u.habitaciones<= ?4 and u.area>=?5 and u.area<=?6")
	   public List<Propiedad> obtenerPropiedadesAvanzado(Long precioMin, Long precioMax,
	    int numhabitacionesMin, int numhabitacionesMax, Long areaMin, Long areaMax);
	
	@Query(value = "SELECT * FROM Propiedad  WHERE propietario = ?1", nativeQuery = true)
	   public List<Propiedad> obtenerPropiedadesUsuario(Long username);
}
