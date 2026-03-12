package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tipos_habitacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"habitaciones"})
public class TipoHabitacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(length = 255)
    private String urlImagen;
    
    @Column(nullable = false)
    private Double precioNoche;
    
    @Column(nullable = false)
    private Integer capacidad;
    
    @OneToMany(mappedBy = "tipoHabitacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Habitacion> habitaciones = new ArrayList<>();
}
