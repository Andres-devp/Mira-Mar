package com.example.demo.service;

import com.example.demo.entities.TipoHabitacion;

import java.util.List;

public interface TipoHabitacionService {

    List<TipoHabitacion> getAllTipos();

    List<TipoHabitacion> filtrarTipos(Integer capacidadMin, Double precioMax);

    TipoHabitacion getTipoById(Long id);

    TipoHabitacion saveTipo(TipoHabitacion tipo);

    void deleteTipo(Long id);
}
