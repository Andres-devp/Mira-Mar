package com.example.demo.service;

import com.example.demo.entities.Room;
import com.example.demo.entities.RoomType;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HabitacionServiceImpl implements HabitacionService {

    @Autowired
    private RoomRepository habitacionRepository;

    @Autowired
    private RoomTypeRepository tipoHabitacionRepository;

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

        if (habitacion.getTipoHabitacion() != null) {
            RoomType tipo = habitacion.getTipoHabitacion();
            tipo.getHabitaciones().remove(habitacion);
            habitacion.setTipoHabitacion(null);
        }

        habitacionRepository.delete(habitacion);
    }
}
