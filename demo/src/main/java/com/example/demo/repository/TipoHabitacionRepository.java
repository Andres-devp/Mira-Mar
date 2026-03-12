package com.example.demo.repository;

import com.example.demo.entities.TipoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Long> {
	Optional<TipoHabitacion> findByCodigoIgnoreCase(String codigo);

	List<TipoHabitacion> findByCapacidadGreaterThanEqual(int capacidad);

	List<TipoHabitacion> findByPrecioNocheLessThanEqual(double precioNoche);

	List<TipoHabitacion> findByCapacidadGreaterThanEqualAndPrecioNocheLessThanEqual(int capacidad, double precioNoche);
}
