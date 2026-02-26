package com.example.demo.repository;

import com.example.demo.entities.Room;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RoomRepository {
    private final Map<Long, Room> rooms = new HashMap<>();
    private Long nextId = 1L;

    public RoomRepository() {
        // Datos falsos de habitaciones de hotel
        addRoom(new Room(null, "Villa Oceanfront", 
            "Lujosa villa frente al mar con terraza privada, piscina infinita y vistas panorámicas al océano. Incluye servicio de mayordomo 24/7.", 
            "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=600", 450.0, 4, "LUXURY"));
        addRoom(new Room(null, "Suite Garden", 
            "Elegante suite con jardín tropical privado, balcón amplio y acceso directo a los jardines del hotel. Perfecta para parejas.", 
            "https://i.pinimg.com/1200x/68/7a/e7/687ae71ad552ffc877c52cf20865a49d.jpg", 320.0, 2, "EXECUTIVE"));
        addRoom(new Room(null, "The Penthouse", 
            "Exclusivo penthouse de dos niveles con terraza en la azotea, jacuzzi privado y vistas de 360 grados al mar y montañas.", 
            "https://i.pinimg.com/1200x/c6/7a/d0/c67ad04072430150a4fb3249ecff3a59.jpg", 850.0, 6, "LUXURY"));
        addRoom(new Room(null, "Deluxe Room", 
            "Habitación deluxe con cama king size, balcón con vista al jardín tropical y amenities de lujo. Ideal para escapadas románticas.", 
            "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600", 280.0, 2, "VIP"));
        addRoom(new Room(null, "Family Suite", 
            "Amplia suite familiar con dos habitaciones, sala de estar separada y cocina compacta. Perfecta para familias con niños.", 
            "https://i.pinimg.com/736x/d4/2b/3f/d42b3f33b58f15af42dbca307a535f05.jpg", 380.0, 5, "VIP"));
        addRoom(new Room(null, "Beach Bungalow", 
            "Acogedor bungalow a pocos pasos de la playa, con porche privado hamacas y acceso directo a la arena. Estilo caribeño auténtico.", 
            "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=600", 250.0, 2, "NORMAL"));
        addRoom(new Room(null, "Ocean View Suite", 
            "Suite con vistas espectaculares al océano, bañera de hidromasaje y balcón amplio con muebles de exterior de lujo.", 
            "https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=600", 420.0, 3, "EXECUTIVE"));
        addRoom(new Room(null, "Garden View Room", 
            "Habitación con vistas a los jardines tropicales, decoración elegante y acceso al spa y piscina principal del hotel.", 
            "https://i.pinimg.com/736x/5e/e1/db/5ee1dbaf2d2bd35010ff264830933837.jpg", 220.0, 2, "NORMAL"));
    }

    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    public Optional<Room> findById(Long id) {
        return Optional.ofNullable(rooms.get(id));
    }

    public Room save(Room room) {
        if (room.getId() == null) {
            room.setId(nextId++);
        }
        rooms.put(room.getId(), room);
        return room;
    }

    private Room addRoom(Room room) {
        // Solo para inicialización
        if (room.getId() == null) {
            room.setId(nextId++);
        }
        rooms.put(room.getId(), room);
        return room;
    }

    public void deleteById(Long id) {
        rooms.remove(id);
    }
}
