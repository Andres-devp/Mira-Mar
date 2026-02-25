package com.example.demo.service;

import com.example.demo.entities.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomTypeService {
    List<RoomType> getAllRoomTypes();

    Optional<RoomType> getRoomTypeByCode(String code);
}
