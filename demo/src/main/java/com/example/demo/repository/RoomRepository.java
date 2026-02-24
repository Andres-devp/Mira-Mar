package com.example.demo.repository;

import com.example.demo.model.Room;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomRepository {
    private final List<Room> rooms = new ArrayList<>();
    private Long nextId = 1L;

    public RoomRepository() {
        // Datos falsos de habitaciones de hotel
        rooms.add(new Room(nextId++, "Villa Oceanfront", 
            "Lujosa villa frente al mar con terraza privada, piscina infinita y vistas panorámicas al océano. Incluye servicio de mayordomo 24/7.", 
            "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=600", 450.0, 4, "Villa"));
        
        rooms.add(new Room(nextId++, "Suite Garden", 
            "Elegante suite con jardín tropical privado, balcón amplio y acceso directo a los jardines del hotel. Perfecta para parejas.", 
            "https://images.unsplash.com/photo-1611892440507-42a792e24d32?w=600", 320.0, 2, "Suite"));
        
        rooms.add(new Room(nextId++, "The Penthouse", 
            "Exclusivo penthouse de dos niveles con terraza en la azotea, jacuzzi privado y vistas de 360 grados al mar y montañas.", 
            "https://images.unsplash.com/photo-1590490360273-2fc32b8dd622?w=600", 850.0, 6, "Penthouse"));
        
        rooms.add(new Room(nextId++, "Deluxe Room", 
            "Habitación deluxe con cama king size, balcón con vista al jardín tropical y amenities de lujo. Ideal para escapadas románticas.", 
            "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600", 280.0, 2, "Deluxe"));
        
        rooms.add(new Room(nextId++, "Family Suite", 
            "Amplia suite familiar con dos habitaciones, sala de estar separada y cocina compacta. Perfecta para familias con niños.", 
            "https://images.unsplash.com/photo-1584132967334-10e028bd69f5?w=600", 380.0, 5, "Family"));
        
        rooms.add(new Room(nextId++, "Beach Bungalow", 
            "Acogedor bungalow a pocos pasos de la playa, con porche privado hamacas y acceso directo a la arena. Estilo caribeño auténtico.", 
            "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=600", 250.0, 2, "Bungalow"));
        
        rooms.add(new Room(nextId++, "Ocean View Suite", 
            "Suite con vistas espectaculares al océano, bañera de hidromasaje y balcón amplio con muebles de exterior de lujo.", 
            "https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=600", 420.0, 3, "Suite"));
        
        rooms.add(new Room(nextId++, "Garden View Room", 
            "Habitación con vistas a los jardines tropicales, decoración elegante y acceso al spa y piscina principal del hotel.", 
            "https://images.unsplash.com/photo-1598928506311-c55ded91e20b?w=600", 220.0, 2, "Standard"));
    }

    public List<Room> findAll() {
        return new ArrayList<>(rooms);
    }

    public Optional<Room> findById(Long id) {
        return rooms.stream()
                .filter(room -> room.getId().equals(id))
                .findFirst();
    }

    public Room save(Room room) {
        if (room.getId() == null) {
            room.setId(nextId++);
            rooms.add(room);
        } else {
            int index = -1;
            for (int i = 0; i < rooms.size(); i++) {
                if (rooms.get(i).getId().equals(room.getId())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                rooms.set(index, room);
            }
        }
        return room;
    }

    public void deleteById(Long id) {
        rooms.removeIf(room -> room.getId().equals(id));
    }
}
