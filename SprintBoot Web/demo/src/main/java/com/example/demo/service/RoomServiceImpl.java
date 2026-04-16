package com.example.demo.service;

import com.example.demo.entities.Reservation;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomType;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository habitacionRepository;

    @Autowired
    private RoomTypeRepository tipoHabitacionRepository;

    @Autowired
    private ReservationRepository reservaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Room> getAllHabitaciones() {
        return habitacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Room getHabitacionById(Long id) {
        return habitacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró habitación con ID: " + id, id));
    }

    @Override
    public Room saveHabitacion(Room habitacion) {
        if (habitacion.getTipoHabitacion() != null && habitacion.getTipoHabitacion().getId() != null) {
            RoomType tipo = tipoHabitacionRepository.findById(habitacion.getTipoHabitacion().getId())
                .orElseThrow(() -> new NotFoundException("No se encontró tipo de habitación con ID: " + habitacion.getTipoHabitacion().getId()));
            habitacion.setTipoHabitacion(tipo);
        }
        return habitacionRepository.save(habitacion);
    }

    @Override
    @Transactional
    public void deleteHabitacion(Long id) {
        Room habitacion = habitacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró habitación con ID: " + id, id));

        List<Reservation> reservasAsociadas = reservaRepository.findByRoomId(id);
        if (!reservasAsociadas.isEmpty()) {
            String reservasResumen = reservasAsociadas.stream()
                .limit(3)
                .map(reserva -> "#" + reserva.getId() + " (" + reserva.getFechaInicio() + " a " + reserva.getFechaFin() + ")")
                .collect(java.util.stream.Collectors.joining(", "));
            String sufijo = reservasAsociadas.size() > 3
                ? " y " + (reservasAsociadas.size() - 3) + " más"
                : "";

            throw new IllegalStateException(
                "No se puede eliminar la habitación '" + habitacion.getNombre() + "' porque tiene "
                    + reservasAsociadas.size() + " reservas asociadas: " + reservasResumen + sufijo
                    + ". Elimina o reasigna las reservas primero."
            );
        }

        if (habitacion.getTipoHabitacion() != null) {
            RoomType tipo = habitacion.getTipoHabitacion();
            tipo.getHabitaciones().remove(habitacion);
            habitacion.setTipoHabitacion(null);
        }

        habitacionRepository.delete(habitacion);
    }
}
