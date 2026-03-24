package com.example.demo.service;

import com.example.demo.entities.RoomType;

import java.util.List;

public interface RoomTypeService {

    List<RoomType> getAllTipos();

    List<RoomType> filtrarTipos(Integer capacidadMin, Double precioMax);

    RoomType getTipoById(Long id);

    RoomType saveTipo(RoomType tipo);

    void deleteTipo(Long id);
}
