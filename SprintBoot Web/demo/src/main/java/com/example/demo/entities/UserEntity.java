package com.example.demo.entities;

import com.example.demo.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
    @UniqueConstraint(columnNames = "usuario"),
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "cedula")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {})
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String usuario;
    
    @Column(nullable = false, length = 255)
    private String contrasena;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 100)
    private String apellido;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(unique = true, length = 20)
    private String cedula;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(length = 255)
    private String fotoPerfil;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole rol;
    
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    
    @Column
    private LocalDateTime ultimaConexion;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
