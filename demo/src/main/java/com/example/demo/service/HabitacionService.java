package com.example.demo.service;

import com.example.demo.entities.Room;

import java.util.List;

public interface HabitacionService {

    List<Room> getAllHabitaciones();

    Room getHabitacionById(Long id);

    Room saveHabitacion(Room habitacion);

    void deleteHabitacion(Long id);
}
