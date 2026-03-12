package com.example.demo.service;

import com.example.demo.entities.TipoHabitacion;

import java.util.List;

public interface TipoHabitacionService {

    List<TipoHabitacion> getAllTipos();

    TipoHabitacion getTipoById(Long id);

    TipoHabitacion saveTipo(TipoHabitacion tipo);

    void deleteTipo(Long id);
}
