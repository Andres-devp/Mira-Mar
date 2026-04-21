package com.example.demo.repository;

import com.example.demo.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByRoomId(Long roomId);
	List<Reservation> findByClientId(Long clientId);
	List<Reservation> findByRoomIdAndFechaInicioLessThanAndFechaFinGreaterThanAndEstadoNot(Long roomId, LocalDate fechaFin, LocalDate fechaInicio, String estado);
}
