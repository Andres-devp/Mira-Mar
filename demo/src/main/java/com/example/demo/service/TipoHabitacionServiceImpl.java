package com.example.demo.service;

import com.example.demo.entities.TipoHabitacion;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.TipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TipoHabitacionServiceImpl implements TipoHabitacionService {

    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TipoHabitacion> getAllTipos() {
        return tipoHabitacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoHabitacion getTipoById(Long id) {
        return tipoHabitacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró tipo de habitación con ID: " + id, id));
    }

    @Override
    public TipoHabitacion saveTipo(TipoHabitacion tipo) {
        return tipoHabitacionRepository.save(tipo);
    }

    @Override
    @Transactional
    public void deleteTipo(Long id) {
        TipoHabitacion tipo = tipoHabitacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró tipo de habitación con ID: " + id, id));

        tipo.getHabitaciones().clear();

        tipoHabitacionRepository.delete(tipo);
    }
}
