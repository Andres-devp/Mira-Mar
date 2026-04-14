package com.example.demo.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Account;
import com.example.demo.entities.AccountItem;
import com.example.demo.entities.Administrator;
import com.example.demo.entities.Client;
import com.example.demo.entities.HotelService;
import com.example.demo.entities.Operator;
import com.example.demo.entities.Reservation;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomType;
import com.example.demo.repository.AccountItemRepository;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.HotelServiceRepository;
import com.example.demo.repository.OperatorRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.RoomTypeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class DataLoader implements CommandLineRunner {
    
    private final RoomTypeRepository tipoHabitacionRepository;
    private final ClientRepository clienteRepository;
        private final AdministratorRepository administradorRepository;
    private final RoomRepository habitacionRepository;
    private final HotelServiceRepository servicioRepository;
        private final OperatorRepository operadorRepository;
        private final ReservationRepository reservaRepository;
        private final AccountRepository cuentaRepository;
        private final AccountItemRepository itemCuentaRepository;
    
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
                .nombre("Cliente Demo")
                .usuario("cliente")
                .contrasena("123")
                .email("cliente@miramar.com")
                .telefono("3000000000")
                .build());

        clientes.add(Client.builder()
                .nombre("Ohcar")
                .usuario("ohca")
                .contrasena("password1234")
                .email("ohcar@gmail.com")
                .telefono("9876543210")
                .build());

        clientes.add(Client.builder()
                .nombre("María García")
                .usuario("maria.garcia")
                .contrasena("pass1234")
                .email("maria.garcia@gmail.com")
                .telefono("555111222")
                .build());

        clientes.add(Client.builder()
                .nombre("Carlos López")
                .usuario("carlos.lopez")
                .contrasena("secure789")
                .email("carlos.lopez@gmail.com")
                .telefono("555333444")
                .build());

        clientes.add(Client.builder()
                .nombre("Ana Martínez")
                .usuario("ana.martinez")
                .contrasena("password555")
                .email("ana.martinez@gmail.com")
                .telefono("555555666")
                .build());

        clientes.add(Client.builder()
                .nombre("Juan Rodríguez")
                .usuario("juan.rodriguez")
                .contrasena("pass1111")
                .email("juan.rodriguez@gmail.com")
                .telefono("555777888")
                .build());

        clientes.add(Client.builder()
                .nombre("Laura Fernández")
                .usuario("laura.fernandez")
                .contrasena("laura123")
                .email("laura.fernandez@gmail.com")
                .telefono("555999000")
                .build());

        clientes.add(Client.builder()
                .nombre("Pedro Sánchez")
                .usuario("pedro.sanchez")
                .contrasena("pedro456")
                .email("pedro.sanchez@gmail.com")
                .telefono("555121212")
                .build());

        clientes.add(Client.builder()
                .nombre("Sofia Gómez")
                .usuario("sofia.gomez")
                .contrasena("sofia789")
                .email("sofia.gomez@gmail.com")
                .telefono("555343434")
                .build());

        clientes.add(Client.builder()
                .nombre("nico")
                .usuario("nico")
                .contrasena("123")
                .email("nico@gmail.com")
                .telefono("555343434")
                .build());
        
        List<Client> clientesGuardados = clienteRepository.saveAll(clientes);

        // 3. Crear 2 administradores
        List<Administrator> administradores = new ArrayList<>();
        administradores.add(Administrator.builder()
                .usuario("admin")
                .contrasena("123")
                .build());

        administradores.add(Administrator.builder()
                .usuario("andres")
                .contrasena("password123456")
                .build());

        List<Administrator> administradoresGuardados = administradorRepository.saveAll(administradores);
        
        // 4. Crear 50 habitaciones con tipos asignados aleatoriamente
        List<Room> habitaciones = new ArrayList<>();
        
        for (int i = 1; i <= 50; i++) {
            RoomType tipoAleatorio = tiposGuardados.get(random.nextInt(tiposGuardados.size()));
            
            habitaciones.add(Room.builder()
                    .nombre("Habitación " + i)
                    .tipoHabitacion(tipoAleatorio)
                    .build());
        }
        
        List<Room> habitacionesGuardadas = habitacionRepository.saveAll(habitaciones);

        // 5. Crear 10 servicios
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
        List<HotelService> serviciosGuardados = servicioRepository.saveAll(servicios);

        // 6. Crear 6 operarios
        List<Operator> operadores = new ArrayList<>();
        operadores.add(Operator.builder()
                .nombre("Operador")
                .apellido("Demo")
                .usuario("operador")
                .email("operador@miramar.com")
                .contrasena("123")
                .cedula("100000000")
                .telefono("3100000000")
                .fotoPerfil("/images/operators/op0.jpg")
                .build());

        operadores.add(Operator.builder()
                .nombre("Carlos")
                .apellido("Ramirez")
                .usuario("carlos")
                .email("carlos.ramirez@miramar.com")
                .contrasena("op12345")
                .cedula("100000001")
                .telefono("3100000001")
                .fotoPerfil("/images/operators/op1.jpg")
                .build());

        operadores.add(Operator.builder()
                .nombre("Daniela")
                .apellido("Mora")
                .usuario("daniela")
                .email("daniela.mora@miramar.com")
                .contrasena("op12346")
                .cedula("100000002")
                .telefono("3100000002")
                .fotoPerfil("/images/operators/op2.jpg")
                .build());

        operadores.add(Operator.builder()
                .nombre("Felipe")
                .apellido("Rojas")
                .usuario("felipe")
                .email("felipe.rojas@miramar.com")
                .contrasena("op12347")
                .cedula("100000003")
                .telefono("3100000003")
                .fotoPerfil("/images/operators/op3.jpg")
                .build());

        operadores.add(Operator.builder()
                .nombre("Valentina")
                .apellido("Sierra")
                .usuario("valentina")
                .email("valentina.sierra@miramar.com")
                .contrasena("op12348")
                .cedula("100000004")
                .telefono("3100000004")
                .fotoPerfil("/images/operators/op4.jpg")
                .build());

        operadores.add(Operator.builder()
                .nombre("Miguel")
                .apellido("Ortega")
                .usuario("miguel")
                .email("miguel.ortega@miramar.com")
                .contrasena("op12349")
                .cedula("100000005")
                .telefono("3100000005")
                .fotoPerfil("/images/operators/op5.jpg")
                .build());

        List<Operator> operadoresGuardados = operadorRepository.saveAll(operadores);

        // 7. Crear 20 reservas (cliente + habitación)
        List<Reservation> reservas = new ArrayList<>();
        String[] estados = {"CONFIRMED", "PENDING", "CANCELED"};
        
        for (int i = 0; i < 20; i++) {
            int clienteIndex = i % clientesGuardados.size();
            int habitacionIndex = i % habitacionesGuardadas.size();
            int estadoIndex = i % estados.length;
            
            LocalDate fechaInicio = LocalDate.of(2026, 4, (i % 25) + 1);
            LocalDate fechaFin = fechaInicio.plusDays((i % 5) + 2);
            int personas = (i % 4) + 1;
            String estado = estados[estadoIndex];
            
            LocalDateTime createdAt = LocalDateTime.now().minusDays(15 - (i % 15));
            LocalDateTime canceledAt = estado.equals("CANCELED") ? createdAt.plusDays(2) : null;
            
            reservas.add(Reservation.builder()
                    .fechaInicio(fechaInicio)
                    .fechaFin(fechaFin)
                    .cantidadPersonas(personas)
                    .estado(estado)
                    .createdAt(createdAt)
                    .canceledAt(canceledAt)
                    .client(clientesGuardados.get(clienteIndex))
                    .room(habitacionesGuardadas.get(habitacionIndex))
                    .build());
        }

        List<Reservation> reservasGuardadas = reservaRepository.saveAll(reservas);

        // 8. Crear 20 cuentas (una por cada reserva)
        List<Account> cuentas = new ArrayList<>();
        for (int i = 0; i < reservasGuardadas.size(); i++) {
            double saldo = reservasGuardadas.get(i).getEstado().equals("CANCELED") 
                ? 0.0 
                : (random.nextDouble() * 1000);
            String estado = reservasGuardadas.get(i).getEstado().equals("CANCELED") 
                ? "CLOSED" 
                : "OPEN";
            
            cuentas.add(Account.builder()
                    .saldo(saldo)
                    .estado(estado)
                    .reservation(reservasGuardadas.get(i))
                    .build());
        }

        List<Account> cuentasGuardadas = cuentaRepository.saveAll(cuentas);

        // 9. Crear 5 items de cuenta (cuenta + servicio existente)
        List<AccountItem> itemsCuenta = new ArrayList<>();
        itemsCuenta.add(AccountItem.builder()
                .cantidad(2)
                .precioUnitario(serviciosGuardados.get(0).getPrice())
                .subtotal(2 * serviciosGuardados.get(0).getPrice())
                .createdAt(LocalDateTime.now().minusDays(4))
                .eliminado(false)
                .account(cuentasGuardadas.get(0))
                .hotelService(serviciosGuardados.get(0))
                .build());

        itemsCuenta.add(AccountItem.builder()
                .cantidad(1)
                .precioUnitario(serviciosGuardados.get(1).getPrice())
                .subtotal(1 * serviciosGuardados.get(1).getPrice())
                .createdAt(LocalDateTime.now().minusDays(3))
                .eliminado(false)
                .account(cuentasGuardadas.get(1))
                .hotelService(serviciosGuardados.get(1))
                .build());

        itemsCuenta.add(AccountItem.builder()
                .cantidad(3)
                .precioUnitario(serviciosGuardados.get(4).getPrice())
                .subtotal(3 * serviciosGuardados.get(4).getPrice())
                .createdAt(LocalDateTime.now().minusDays(2))
                .eliminado(false)
                .account(cuentasGuardadas.get(2))
                .hotelService(serviciosGuardados.get(4))
                .build());

        itemsCuenta.add(AccountItem.builder()
                .cantidad(1)
                .precioUnitario(serviciosGuardados.get(6).getPrice())
                .subtotal(1 * serviciosGuardados.get(6).getPrice())
                .createdAt(LocalDateTime.now().minusDays(2))
                .eliminado(false)
                .account(cuentasGuardadas.get(3))
                .hotelService(serviciosGuardados.get(6))
                .build());

        itemsCuenta.add(AccountItem.builder()
                .cantidad(2)
                .precioUnitario(serviciosGuardados.get(3).getPrice())
                .subtotal(2 * serviciosGuardados.get(3).getPrice())
                .createdAt(LocalDateTime.now().minusDays(1))
                .eliminado(false)
                .account(cuentasGuardadas.get(4))
                .hotelService(serviciosGuardados.get(3))
                .build());

        List<AccountItem> itemsCuentaGuardados = itemCuentaRepository.saveAll(itemsCuenta);

        System.out.println(" Datos inicializados: " +
                tiposGuardados.size() + " tipos, " +
                administradoresGuardados.size() + " administradores, " +
                clientes.size() + " clientes, " +
                habitaciones.size() + " habitaciones, " +
                servicios.size() + " servicios, " +
                operadoresGuardados.size() + " operarios, " +
                reservasGuardadas.size() + " reservas, " +
                cuentasGuardadas.size() + " cuentas, " +
                itemsCuentaGuardados.size() + " itemsCuenta");
    }
}
