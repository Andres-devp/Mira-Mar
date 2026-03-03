package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entities.RoomType;
import com.example.demo.repository.RoomTypeRepository;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;

    private String normalizeCode(String code) {
        if (code == null) return null;
        String normalized = code.trim();
        if (normalized.isEmpty()) return null;
        return normalized.toUpperCase();
    }

    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    @Override
    public Optional<RoomType> getRoomTypeByCode(String code) {
        return roomTypeRepository.findByCode(normalizeCode(code));
    }

    @Override
    public void saveRoomType(RoomType type) {
        if (type == null) return;
        type.setCode(normalizeCode(type.getCode()));
        roomTypeRepository.save(type);
    }

    @Override
    public void deleteByCode(String code) {
        roomTypeRepository.deleteByCode(normalizeCode(code));
    }
}
