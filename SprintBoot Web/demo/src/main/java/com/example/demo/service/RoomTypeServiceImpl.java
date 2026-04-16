package com.example.demo.service;

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
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    private RoomTypeRepository tipoHabitacionRepository;

    @Autowired
    private RoomRepository habitacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RoomType> getAllTipos() {
        return tipoHabitacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomType> filtrarTipos(Integer capacidadMin, Double precioMax) {
        if (capacidadMin != null && precioMax != null) {
            return tipoHabitacionRepository.findByCapacidadGreaterThanEqualAndPrecioNocheLessThanEqual(capacidadMin, precioMax);
        } else if (capacidadMin != null) {
            return tipoHabitacionRepository.findByCapacidadGreaterThanEqual(capacidadMin);
        } else if (precioMax != null) {
            return tipoHabitacionRepository.findByPrecioNocheLessThanEqual(precioMax);
        } else {
            return tipoHabitacionRepository.findAll();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RoomType getTipoById(Long id) {
        return tipoHabitacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró tipo de habitación con ID: " + id, id));
    }

    @Override
    public RoomType saveTipo(RoomType tipo) {
        return tipoHabitacionRepository.save(tipo);
    }

    @Override
    @Transactional
    public void deleteTipo(Long id) {
        tipoHabitacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró tipo de habitación con ID: " + id, id));

        List<com.example.demo.entities.Room> habitacionesAsociadas = habitacionRepository.findByTipoHabitacionId(id);
        if (!habitacionesAsociadas.isEmpty()) {
            String nombres = habitacionesAsociadas.stream()
                .limit(3)
                .map(com.example.demo.entities.Room::getNombre)
                .collect(java.util.stream.Collectors.joining(", "));
            String sufijo = habitacionesAsociadas.size() > 3
                ? " y " + (habitacionesAsociadas.size() - 3) + " más"
                : "";
            throw new IllegalStateException(
                "No se puede eliminar el tipo porque tiene " + habitacionesAsociadas.size()
                + " habitaciones asociadas: " + nombres + sufijo
                + ". Elimina o reasigna las habitaciones primero."
            );
        }

        tipoHabitacionRepository.deleteById(id);
    }
}
