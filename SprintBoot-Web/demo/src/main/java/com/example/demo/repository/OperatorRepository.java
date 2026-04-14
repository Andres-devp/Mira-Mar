package com.example.demo.repository;

import com.example.demo.entities.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {
	Optional<Operator> findByUsuario(String usuario);
	Optional<Operator> findByEmail(String email);
	Optional<Operator> findByCedula(String cedula);
}
