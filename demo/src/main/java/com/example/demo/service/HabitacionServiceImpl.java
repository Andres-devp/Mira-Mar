package com.example.demo.service;

import com.example.demo.entities.Habitacion;
import com.example.demo.entities.TipoHabitacion;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.HabitacionRepository;
import com.example.demo.repository.TipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HabitacionServiceImpl implements HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Habitacion> getAllHabitaciones() {
        return habitacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Habitacion getHabitacionById(Long id) {
        return habitacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró habitación con ID: " + id, id));
    }

    @Override
    public Habitacion saveHabitacion(Habitacion habitacion) {
        if (habitacion.getTipoHabitacion() != null && habitacion.getTipoHabitacion().getId() != null) {
            TipoHabitacion tipo = tipoHabitacionRepository.findById(habitacion.getTipoHabitacion().getId())
                .orElseThrow(() -> new NotFoundException("No se encontró tipo de habitación con ID: " + habitacion.getTipoHabitacion().getId()));
            habitacion.setTipoHabitacion(tipo);
        }
        return habitacionRepository.save(habitacion);
    }

    @Override
    @Transactional
    public void deleteHabitacion(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró habitación con ID: " + id, id));

        if (habitacion.getTipoHabitacion() != null) {
            TipoHabitacion tipo = habitacion.getTipoHabitacion();
            tipo.getHabitaciones().remove(habitacion);
            habitacion.setTipoHabitacion(null);
        }

        habitacionRepository.delete(habitacion);
    }
}
