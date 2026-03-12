package com.example.demo.service;

import com.example.demo.entities.Habitacion;

import java.util.List;

public interface HabitacionService {

    List<Habitacion> getAllHabitaciones();

    Habitacion getHabitacionById(Long id);

    Habitacion saveHabitacion(Habitacion habitacion);

    void deleteHabitacion(Long id);
}
