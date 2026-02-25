package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Double pricePerNight;
    private Integer capacity;
    private String roomType;
}
