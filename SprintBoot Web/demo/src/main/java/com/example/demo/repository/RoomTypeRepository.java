package com.example.demo.repository;

import com.example.demo.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
	Optional<RoomType> findByCodigoIgnoreCase(String codigo);

	List<RoomType> findByCapacidadGreaterThanEqual(int capacidad);

	List<RoomType> findByPrecioNocheLessThanEqual(double precioNoche);

	List<RoomType> findByCapacidadGreaterThanEqualAndPrecioNocheLessThanEqual(int capacidad, double precioNoche);
}
