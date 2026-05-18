package com.example.demo.repository;

import com.example.demo.entities.UserEntity;
import com.example.demo.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsuario(String usuario);
    
    Optional<UserEntity> findByEmail(String email);
    
    Optional<UserEntity> findByCedula(String cedula);
    
    List<UserEntity> findByRol(UserRole rol);
    
    List<UserEntity> findByActivo(Boolean activo);
    
    List<UserEntity> findByRolAndActivo(UserRole rol, Boolean activo);
    
    boolean existsByUsuario(String usuario);
    
    boolean existsByEmail(String email);
    
    boolean existsByCedula(String cedula);
}
