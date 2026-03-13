package com.example.demo.config;

import com.example.demo.entities.Client;
import com.example.demo.entities.Room;
import com.example.demo.entities.HotelService;
import com.example.demo.entities.RoomType;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.HotelServiceRepository;
import com.example.demo.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Transactional
public class DataLoader implements CommandLineRunner {
    
    private final RoomTypeRepository tipoHabitacionRepository;
    private final ClientRepository clienteRepository;
    private final RoomRepository habitacionRepository;
    private final HotelServiceRepository servicioRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (tipoHabitacionRepository.count() == 0) {
            cargarDatos();
        }
    }
    
    private void cargarDatos() {
        Random random = new Random(42);
        
        // 1. Crear 5 tipos de habitación
        List<RoomType> tipos = new ArrayList<>();
        tipos.add(RoomType.builder()
                .codigo("ESTANDAR")
                .nombre("Habitación Estándar")
                .descripcion("Habitación básica confortable para una o dos personas")
                .urlImagen("/images/Habitacion1.avif")
                .precioNoche(80.0)
                .capacidad(2)
                .build());
        
        tipos.add(RoomType.builder()
                .codigo("DOBLE")
                .nombre("Habitación Doble")
                .descripcion("Amplia habitación con cama doble y amenidades")
                .urlImagen("/images/Habitacion2.avif")
                .precioNoche(120.0)
                .capacidad(2)
                .build());
        
        tipos.add(RoomType.builder()
                .codigo("SUITE")
                .nombre("Suite Ejecutiva")
                .descripcion("Suite de lujo con sala de estar y vistas al mar")
                .urlImagen("/images/Habitacion3.avif")
                .precioNoche(200.0)
                .capacidad(3)
                .build());
        
        tipos.add(RoomType.builder()
                .codigo("FAMILIAR")
                .nombre("Habitación Familiar")
                .descripcion("Espaciosa habitación para familias con múltiples camas")
                .urlImagen("/images/habitacionFamiliar.jpg")
                .precioNoche(180.0)
                .capacidad(4)
                .build());
        
        tipos.add(RoomType.builder()
                .codigo("PRESIDENCIAL")
                .nombre("Suite Presidencial")
                .descripcion("La mejor suite del hotel con lujos y servicios premium")
                .urlImagen("/images/Hero.avif")
                .precioNoche(350.0)
                .capacidad(4)
                .build());
        
        List<RoomType> tiposGuardados = tipoHabitacionRepository.saveAll(tipos);
        
        // 2. Crear 10 clientes
        List<Client> clientes = new ArrayList<>();
        clientes.add(Client.builder()
                .nombre("Administrador")
                .usuario("operador")
                .contrasena("123")
                .rol("ADMIN")
                .email("admin@miramar.com")
                .telefono("0000000000")
                .build());
        
        clientes.add(Client.builder()
                .nombre("Andres Doncel")
                .usuario("andres")
                .contrasena("password123456")
                .rol("ADMIN")
                .email("andres@gmail.com")
                .telefono("1234567890")
                .build());
        
        clientes.add(Client.builder()
                .nombre("Ohcar")
                .usuario("ohca")
                .contrasena("password1234")
                .rol("CLIENTE")
                .email("ohcar@gmail.com")
                .telefono("9876543210")
                .build());
        
        clientes.add(Client.builder()
                .nombre("María García")
                .usuario("maria.garcia")
                .contrasena("pass1234")
                .rol("CLIENTE")
                .email("maria.garcia@gmail.com")
                .telefono("555111222")
                .build());
        
        clientes.add(Client.builder()
                .nombre("Carlos López")
                .usuario("carlos.lopez")
                .contrasena("secure789")
                .rol("CLIENTE")
                .email("carlos.lopez@gmail.com")
                .telefono("555333444")
                .build());
        
        clientes.add(Client.builder()
                .nombre("Ana Martínez")
                .usuario("ana.martinez")
                .contrasena("password555")
                .rol("CLIENTE")
                .email("ana.martinez@gmail.com")
                .telefono("555555666")
                .build());
        
        clientes.add(Client.builder()
                .nombre("Juan Rodríguez")
                .usuario("juan.rodriguez")
                .contrasena("pass1111")
                .rol("CLIENTE")
                .email("juan.rodriguez@gmail.com")
                .telefono("555777888")
                .build());
        
        clientes.add(Client.builder()
                .nombre("Laura Fernández")
                .usuario("laura.fernandez")
                .contrasena("laura123")
                .rol("CLIENTE")
                .email("laura.fernandez@gmail.com")
                .telefono("555999000")
                .build());
        
        clientes.add(Client.builder()
                .nombre("Pedro Sánchez")
                .usuario("pedro.sanchez")
                .contrasena("pedro456")
                .rol("CLIENTE")
                .email("pedro.sanchez@gmail.com")
                .telefono("555121212")
                .build());
        
        clientes.add(Client.builder()
                .nombre("Sofia Gómez")
                .usuario("sofia.gomez")
                .contrasena("sofia789")
                .rol("CLIENTE")
                .email("sofia.gomez@gmail.com")
                .telefono("555343434")
                .build());

        clientes.add(Client.builder()
                .nombre("nico")
                .usuario("nico")
                .contrasena("123")
                .rol("CLIENTE")
                .email("nico@gmail.com")
                .telefono("555343434")
                .build());
        
        clienteRepository.saveAll(clientes);
        
        // 3. Crear 50 habitaciones con tipos asignados aleatoriamente
        List<Room> habitaciones = new ArrayList<>();
        
        for (int i = 1; i <= 50; i++) {
            RoomType tipoAleatorio = tiposGuardados.get(random.nextInt(tiposGuardados.size()));
            
            habitaciones.add(Room.builder()
                    .nombre("Habitación " + i)
                    .tipoHabitacion(tipoAleatorio)
                    .build());
        }
        
        habitacionRepository.saveAll(habitaciones);

        // 4. Crear 10 servicios
        List<HotelService> servicios = new ArrayList<>();
        servicios.add(HotelService.builder().nombre("Restaurante").descripcion("Restaurante gourmet con menú internacional").imageUrl("https://i.pinimg.com/736x/49/be/79/49be795193f4d6bd20b7a7d1dbc644f3.jpg").price(45.0).build());
        servicios.add(HotelService.builder().nombre("Clases de surf").descripcion("Clases de surf para todos los niveles").imageUrl("https://mojosurf.es/wp-content/uploads/2024/12/Clases-de-surf-que-tener-en-cuenta.jpg").price(30.0).build());
        servicios.add(HotelService.builder().nombre("Caminatas guiadas").descripcion("Excursiones y caminatas por la naturaleza").imageUrl("https://i.pinimg.com/736x/17/95/d9/1795d9b9e9734035ea365debecc48267.jpg").price(20.0).build());
        servicios.add(HotelService.builder().nombre("Spa & Wellness").descripcion("Masajes, sauna y tratamientos de spa").imageUrl("https://i.pinimg.com/736x/91/9a/fc/919afcf0663bf853bf584e8672166dd0.jpg").price(80.0).build());
        servicios.add(HotelService.builder().nombre("Alquiler de bicicletas").descripcion("Bicicletas para recorrer la zona").imageUrl("https://i.pinimg.com/736x/54/26/0d/54260d946194dd1ac4a500cda97194ad.jpg").price(15.0).build());
        servicios.add(HotelService.builder().nombre("Piscina").descripcion("Piscina exterior con bar y solárium").imageUrl("https://i.pinimg.com/736x/04/35/9b/04359b99919a8debaaba2173b988927d.jpg").price(0.0).build());
        servicios.add(HotelService.builder().nombre("Transporte al aeropuerto").descripcion("Servicio de traslado desde/hacia el aeropuerto").imageUrl("https://i.pinimg.com/736x/1d/1c/95/1d1c9548a787dbf89974fc4e957d5a13.jpg").price(25.0).build());
        servicios.add(HotelService.builder().nombre("Bar en la playa").descripcion("Bar con cócteles y snacks en la playa").imageUrl("https://i.pinimg.com/736x/5c/c3/d4/5cc3d4c5021bdaf8ebdcbfdf33f4757f.jpg").price(10.0).build());
        servicios.add(HotelService.builder().nombre("Club infantil").descripcion("Actividades y juegos para niños").imageUrl("https://i.pinimg.com/736x/02/46/41/024641a99c2aada4e851b2ebd80e3a13.jpg").price(5.0).build());
        servicios.add(HotelService.builder().nombre("Eventos y bodas").descripcion("Organización de eventos y bodas en el hotel").imageUrl("https://i.pinimg.com/736x/4e/2a/90/4e2a90524e644f8f95784f3b805d06ae.jpg").price(200.0).build());
        servicioRepository.saveAll(servicios);

        System.out.println("✅ Datos inicializados: " +
                tiposGuardados.size() + " tipos, " +
                clientes.size() + " clientes, " +
                habitaciones.size() + " habitaciones, " +
                servicios.size() + " servicios");
    }
}
