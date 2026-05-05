package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.entities.Room;
import com.example.demo.entities.RoomType;

import static org.mockito.ArgumentMatchers.isNotNull;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.RequiredArgsConstructor;

@DataJpaTest
public class RoomTypeRepositoryTest {


    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @BeforeEach
    public void setUp() {
        roomTypeRepository.save(RoomType.builder()
                .codigo("ESTANDAR")
                .nombre("Habitación Estándar")
                .descripcion("Habitación básica confortable para una o dos personas")
                .urlImagen("/images/Habitacion1.avif")
                .precioNoche(80.0)
                .capacidad(2)
                .build());

        roomTypeRepository.save(RoomType.builder()
                .codigo("OTRA")
                .nombre("Habitación OTRA")
                .descripcion("Habitación OTRA para una o dos personas")
                .urlImagen("/images/Habitacion1.avif")
                .precioNoche(80.0)
                .capacidad(4)
                .build());

                roomTypeRepository.save(RoomType.builder()
                .codigo("ESTANDAR2")
                .nombre("Habitación Estándar2")
                .descripcion("Habitación básica2 confortable para una o dos personas")
                .urlImagen("/images/Habitacion1.avif")
                .precioNoche(100.0)
                .capacidad(6)
                .build());

        roomTypeRepository.save(RoomType.builder()
                .codigo("OTRA3")
                .nombre("Habitación OTRA3")
                .descripcion("Habitación OTRA para una o dos personas")
                .urlImagen("/images/Habitacion1.avif")
                .precioNoche(150.0)
                .capacidad(1)
                .build());
    }

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

        @Test
    public void RoomTypeRepository_save_Error() {

        RoomType roomType =  roomTypeRepository.save(RoomType.builder()
                .codigo("NUEVO")
                .nombre("Habitación Estándar")
                .descripcion("Habitación básica confortable para una o dos personas")
                .urlImagen("/images/Habitacion1.avif")
                .precioNoche(80.0)
                .capacidad(2)
                .build());

         Assertions.assertThat(roomType).isNotNull();       

    }

        @Test
    public void RoomTypeRepository_findAll_NotEmptyList() {

        List<RoomType> roomTypes = roomTypeRepository.findAll();
        Assertions.assertThat(roomTypes).isNotEmpty();
        Assertions.assertThat(roomTypes.size()).isEqualTo(4);

    }

    @Test
    public void RoomTypeRepository_findById_findWrongIndex() {

        Long id=-1L; 
        Optional<RoomType> roomType = roomTypeRepository.findById(id);
        Assertions.assertThat(roomType).isEmpty();

    }

    @Test
    public void RoomTypeRepository_deleteById_EmptyRoomType() {

        Long id=4L; 
        roomTypeRepository.deleteById(id);

        Assertions.assertThat(roomTypeRepository.findById(id)).isEmpty();

    }

    @Test
    public void RoomTypeRepository_findByCapacidadGreaterThanEqual_NotEmptyList() {

        int capacidad = 3;
        List<RoomType> roomTypes = roomTypeRepository.findByCapacidadGreaterThanEqual(capacidad);
        Assertions.assertThat(roomTypes).isNotEmpty();
        Assertions.assertThat(roomTypes.size()).isEqualTo(2);

    }

        @Test
    public void RoomTypeRepository_findByPrecioNocheLessThanEqual_NotEmptyList() {

        int precioNoche = 125;
        List<RoomType> roomTypes = roomTypeRepository.findByPrecioNocheLessThanEqual(precioNoche);
        Assertions.assertThat(roomTypes).isNotEmpty();
        Assertions.assertThat(roomTypes.size()).isEqualTo(3);
    }

            @Test
    public void RoomTypeRepository_findByCapacidadGreaterThanEqualAndPrecioNocheLessThanEqual_NotEmptyList() {

        int precioNoche = 125;
        int capacidad = 3;
        List<RoomType> roomTypes = roomTypeRepository.findByCapacidadGreaterThanEqualAndPrecioNocheLessThanEqual(capacidad, precioNoche);
        Assertions.assertThat(roomTypes).isNotEmpty();
        Assertions.assertThat(roomTypes.size()).isEqualTo(2);
    }



















    
}
