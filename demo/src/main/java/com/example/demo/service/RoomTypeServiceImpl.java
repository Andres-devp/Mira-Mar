package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entities.RoomType;
import com.example.demo.repository.RoomTypeRepository;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    @Override
    public Optional<RoomType> getRoomTypeByCode(String code) {
        return roomTypeRepository.findByCode(code);
    }

    @Override
    public void saveRoomType(RoomType type) {
        if (type != null && type.getCode() != null) {
            roomTypeRepository.save(type);
        }
    }

    @Override
    public void deleteByCode(String code) {
        if (code != null) {
            roomTypeRepository.deleteByCode(code);
        }
    }
}
