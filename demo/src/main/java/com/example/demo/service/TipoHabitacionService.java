package com.example.demo.service;

import com.example.demo.entities.TipoHabitacion;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.TipoHabitacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoHabitacionService {
    
    private final TipoHabitacionRepository tipoHabitacionRepository;
    
    @Transactional(readOnly = true)
    public List<TipoHabitacion> getAllTipos() {
        return tipoHabitacionRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public TipoHabitacion getTipoById(Long id) {
        return tipoHabitacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró tipo de habitación con ID: " + id, id));
    }
    
    @Transactional(readOnly = true)
    public Optional<TipoHabitacion> findTipoById(Long id) {
        return tipoHabitacionRepository.findById(id);
    }
    
    public TipoHabitacion saveTipo(TipoHabitacion tipo) {
        return tipoHabitacionRepository.save(tipo);
    }
    
    @Transactional
    public void deleteTipo(Long id) {
        TipoHabitacion tipo = tipoHabitacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró tipo de habitación con ID: " + id, id));
        
        // Vaciar la lista de habitaciones
        tipo.getHabitaciones().clear();
        
        tipoHabitacionRepository.delete(tipo);
    }
}
