package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.RoomType;
import com.example.demo.entities.TipoHabitacion;

@Repository
public class RoomTypeRepository {
    private final TipoHabitacionRepository tipoHabitacionRepository;

    public RoomTypeRepository(TipoHabitacionRepository tipoHabitacionRepository) {
        this.tipoHabitacionRepository = tipoHabitacionRepository;
    }

    private String normalizeCode(String code) {
        if (code == null) return null;
        String normalized = code.trim();
        if (normalized.isEmpty()) return null;
        return normalized.toUpperCase();
    }

    private RoomType toRoomType(TipoHabitacion tipoHabitacion) {
        return new RoomType(
                tipoHabitacion.getCodigo(),
                tipoHabitacion.getNombre(),
                tipoHabitacion.getDescripcion()
        );
    }

    public List<RoomType> findAll() {
        List<TipoHabitacion> tipos = tipoHabitacionRepository.findAll();
        List<RoomType> roomTypes = new ArrayList<>(tipos.size());
        for (TipoHabitacion tipo : tipos) {
            roomTypes.add(toRoomType(tipo));
        }
        return roomTypes;
    }

    public Optional<RoomType> findByCode(String code) {
        String normalizedCode = normalizeCode(code);
        if (normalizedCode == null) return Optional.empty();
        return tipoHabitacionRepository.findByCodigoIgnoreCase(normalizedCode)
                .map(this::toRoomType);
    }

    public void save(RoomType type) {
        if (type == null) return;
        String normalizedCode = normalizeCode(type.getCode());
        if (normalizedCode == null) return;

        TipoHabitacion entidad = tipoHabitacionRepository.findByCodigoIgnoreCase(normalizedCode)
                .orElseGet(TipoHabitacion::new);

        entidad.setCodigo(normalizedCode);
        entidad.setNombre(type.getDisplayName());
        entidad.setDescripcion(type.getDescription());
        tipoHabitacionRepository.save(entidad);
    }

    public void deleteByCode(String code) {
        String normalizedCode = normalizeCode(code);
        if (normalizedCode == null) return;

        tipoHabitacionRepository.findByCodigoIgnoreCase(normalizedCode)
                .ifPresent(tipoHabitacionRepository::delete);
    }
}
