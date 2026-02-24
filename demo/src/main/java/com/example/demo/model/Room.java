package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
