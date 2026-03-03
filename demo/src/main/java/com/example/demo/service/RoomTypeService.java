package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entities.RoomType;

public interface RoomTypeService {
    List<RoomType> getAllRoomTypes();

    Optional<RoomType> getRoomTypeByCode(String code);

    void saveRoomType(RoomType type);

    void deleteByCode(String code);
}
