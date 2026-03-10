package com.example.demo.service;

import com.example.demo.entities.Habitacion;
import com.example.demo.entities.TipoHabitacion;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.HabitacionRepository;
import com.example.demo.repository.TipoHabitacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HabitacionService {
    
    private final HabitacionRepository habitacionRepository;
    private final TipoHabitacionRepository tipoHabitacionRepository;
    
    @Transactional(readOnly = true)
    public List<Habitacion> getAllHabitaciones() {
        return habitacionRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Habitacion getHabitacionById(Long id) {
        return habitacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró habitación con ID: " + id, id));
    }
    
    @Transactional(readOnly = true)
    public Optional<Habitacion> findHabitacionById(Long id) {
        return habitacionRepository.findById(id);
    }
    
    public Habitacion saveHabitacion(Habitacion habitacion) {
        if (habitacion.getTipoHabitacion() != null && habitacion.getTipoHabitacion().getId() != null) {
            TipoHabitacion tipo = tipoHabitacionRepository.findById(habitacion.getTipoHabitacion().getId())
                .orElseThrow(() -> new NotFoundException("No se encontró tipo de habitación con ID: " + habitacion.getTipoHabitacion().getId()));
            habitacion.setTipoHabitacion(tipo);
        }
        return habitacionRepository.save(habitacion);
    }
    
    @Transactional
    public void deleteHabitacion(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró habitación con ID: " + id, id));
        
        // Desasociar de TipoHabitacion
        if (habitacion.getTipoHabitacion() != null) {
            TipoHabitacion tipo = habitacion.getTipoHabitacion();
            tipo.getHabitaciones().remove(habitacion);
            habitacion.setTipoHabitacion(null);
        }
        
        habitacionRepository.delete(habitacion);
    }
}
