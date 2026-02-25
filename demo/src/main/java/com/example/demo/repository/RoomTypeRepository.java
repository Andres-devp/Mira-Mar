package com.example.demo.repository;

import com.example.demo.entities.RoomType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class RoomTypeRepository {
    private final Map<String, RoomType> roomTypes = new LinkedHashMap<>();

    public RoomTypeRepository() {
        roomTypes.put("NORMAL", new RoomType(
                "NORMAL",
                "Normal",
                "Habitación estándar con comodidades esenciales para una estadía cómoda."
        ));
        roomTypes.put("EXECUTIVE", new RoomType(
                "EXECUTIVE",
                "Executive",
                "Habitación con espacio adicional y comodidades superiores para viajes de negocios o parejas."
        ));
        roomTypes.put("VIP", new RoomType(
                "VIP",
                "VIP",
                "Habitación premium con servicios preferenciales y mejores vistas/amenidades."
        ));
        roomTypes.put("LUXURY", new RoomType(
                "LUXURY",
                "Luxury",
                "Habitación de lujo con la experiencia más exclusiva del hotel."
        ));
    }

    public List<RoomType> findAll() {
        return new ArrayList<>(roomTypes.values());
    }

    public Optional<RoomType> findByCode(String code) {
        if (code == null) return Optional.empty();
        return Optional.ofNullable(roomTypes.get(code));
    }
}
