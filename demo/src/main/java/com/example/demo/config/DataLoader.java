package com.example.demo.config;

import com.example.demo.entities.Cliente;
import com.example.demo.entities.Habitacion;
import com.example.demo.entities.TipoHabitacion;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.HabitacionRepository;
import com.example.demo.repository.TipoHabitacionRepository;
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
    
    private final TipoHabitacionRepository tipoHabitacionRepository;
    private final ClienteRepository clienteRepository;
    private final HabitacionRepository habitacionRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (tipoHabitacionRepository.count() == 0) {
            cargarDatos();
        }
    }
    
    private void cargarDatos() {
        Random random = new Random(42);
        
        // 1. Crear 5 tipos de habitación
        List<TipoHabitacion> tipos = new ArrayList<>();
        tipos.add(TipoHabitacion.builder()
                .codigo("ESTANDAR")
                .nombre("Habitación Estándar")
                .descripcion("Habitación básica confortable para una o dos personas")
                .build());
        
        tipos.add(TipoHabitacion.builder()
                .codigo("DOBLE")
                .nombre("Habitación Doble")
                .descripcion("Amplia habitación con cama doble y amenidades")
                .build());
        
        tipos.add(TipoHabitacion.builder()
                .codigo("SUITE")
                .nombre("Suite Ejecutiva")
                .descripcion("Suite de lujo con sala de estar y vistas al mar")
                .build());
        
        tipos.add(TipoHabitacion.builder()
                .codigo("FAMILIAR")
                .nombre("Habitación Familiar")
                .descripcion("Espaciosa habitación para familias con múltiples camas")
                .build());
        
        tipos.add(TipoHabitacion.builder()
                .codigo("PRESIDENCIAL")
                .nombre("Suite Presidencial")
                .descripcion("La mejor suite del hotel con lujos y servicios premium")
                .build());
        
        List<TipoHabitacion> tiposGuardados = tipoHabitacionRepository.saveAll(tipos);
        
        // 2. Crear 10 clientes
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(Cliente.builder()
                .nombre("Administrador")
                .usuario("admin")
                .contrasena("admin123")
                .rol("ADMIN")
                .email("admin@miramar.com")
                .telefono("0000000000")
                .build());
        
        clientes.add(Cliente.builder()
                .nombre("Andres Doncel")
                .usuario("andres")
                .contrasena("password123456")
                .rol("ADMIN")
                .email("andres@gmail.com")
                .telefono("1234567890")
                .build());
        
        clientes.add(Cliente.builder()
                .nombre("Ohcar")
                .usuario("ohca")
                .contrasena("password1234")
                .rol("CLIENTE")
                .email("ohcar@gmail.com")
                .telefono("9876543210")
                .build());
        
        clientes.add(Cliente.builder()
                .nombre("María García")
                .usuario("maria.garcia")
                .contrasena("pass1234")
                .rol("CLIENTE")
                .email("maria.garcia@gmail.com")
                .telefono("555111222")
                .build());
        
        clientes.add(Cliente.builder()
                .nombre("Carlos López")
                .usuario("carlos.lopez")
                .contrasena("secure789")
                .rol("CLIENTE")
                .email("carlos.lopez@gmail.com")
                .telefono("555333444")
                .build());
        
        clientes.add(Cliente.builder()
                .nombre("Ana Martínez")
                .usuario("ana.martinez")
                .contrasena("password555")
                .rol("CLIENTE")
                .email("ana.martinez@gmail.com")
                .telefono("555555666")
                .build());
        
        clientes.add(Cliente.builder()
                .nombre("Juan Rodríguez")
                .usuario("juan.rodriguez")
                .contrasena("pass1111")
                .rol("CLIENTE")
                .email("juan.rodriguez@gmail.com")
                .telefono("555777888")
                .build());
        
        clientes.add(Cliente.builder()
                .nombre("Laura Fernández")
                .usuario("laura.fernandez")
                .contrasena("laura123")
                .rol("CLIENTE")
                .email("laura.fernandez@gmail.com")
                .telefono("555999000")
                .build());
        
        clientes.add(Cliente.builder()
                .nombre("Pedro Sánchez")
                .usuario("pedro.sanchez")
                .contrasena("pedro456")
                .rol("CLIENTE")
                .email("pedro.sanchez@gmail.com")
                .telefono("555121212")
                .build());
        
        clientes.add(Cliente.builder()
                .nombre("Sofia Gómez")
                .usuario("sofia.gomez")
                .contrasena("sofia789")
                .rol("CLIENTE")
                .email("sofia.gomez@gmail.com")
                .telefono("555343434")
                .build());
        
        clienteRepository.saveAll(clientes);
        
        // 3. Crear 50 habitaciones con tipos asignados aleatoriamente
        List<Habitacion> habitaciones = new ArrayList<>();
        String[] imagenes = {
            "Habitacion1.avif",
            "Habitacion2.avif",
            "Habitacion3.avif",
            "Ocean View Interior.avif",
            "Hero.avif"
        };
        
        for (int i = 1; i <= 50; i++) {
            TipoHabitacion tipoAleatorio = tiposGuardados.get(random.nextInt(tiposGuardados.size()));
            int capacidad = random.nextInt(3) + 1; // 1 a 3 personas
            double precio = 50.0 + (random.nextDouble() * 200.0); // 50 a 250 por noche
            
            habitaciones.add(Habitacion.builder()
                    .nombre("Habitación " + i)
                    .descripcion("Habitación confortable " + i + " con excelentes servicios")
                    .urlImagen("/images/" + imagenes[random.nextInt(imagenes.length)])
                    .precioNoche(Math.round(precio * 100.0) / 100.0)
                    .capacidad(capacidad)
                    .tipoHabitacion(tipoAleatorio)
                    .build());
        }
        
        habitacionRepository.saveAll(habitaciones);
        
        System.out.println("✅ Datos inicializados: " +
                tiposGuardados.size() + " tipos, " +
                clientes.size() + " clientes, " +
                habitaciones.size() + " habitaciones");
    }
}
