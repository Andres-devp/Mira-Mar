package com.example.demo.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.entities.Room;
import com.example.demo.entities.RoomType;

import static org.mockito.ArgumentMatchers.isNotNull;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;

@DataJpaTest
public class RoomTypeRepositoryTest {


    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Test
    public void RoomTypeRepository_save_roomType() {

        RoomType roomType =  roomTypeRepository.save(RoomType.builder()
                .codigo("ESTANDAR")
                .nombre("Habitación Estándar")
                .descripcion("Habitación básica confortable para una o dos personas")
                .urlImagen("/images/Habitacion1.avif")
                .precioNoche(80.0)
                .capacidad(2)
                .build());

         Assertions.assertThat(roomType).isNotNull();       

    }

    
}
