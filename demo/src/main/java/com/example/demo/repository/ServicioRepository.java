package com.example.demo.repository;

import com.example.demo.entities.Servicio;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ServicioRepository {
    private final Map<Long, Servicio> servicios = new HashMap<>();
    private Long nextId = 1L;

    public ServicioRepository() {
        // 10 servicios de ejemplo
        addServicio(new Servicio("https://mojosurf.es/wp-content/uploads/2024/12/Clases-de-surf-que-tener-en-cuenta.jpg", "Restaurante", "Restaurante gourmet con menú internacional"));
        addServicio(new Servicio(null, "Clases de surf", "Clases de surf para todos los niveles"));
        addServicio(new Servicio(null, "Caminatas guiadas", "Excursiones y caminatas por la naturaleza"));
        addServicio(new Servicio(null, "Spa & Wellness", "Masajes, sauna y tratamientos de spa"));
        addServicio(new Servicio(null, "Alquiler de bicicletas", "Bicicletas para recorrer la zona"));
        addServicio(new Servicio(null, "Piscina", "Piscina exterior con bar y solárium"));
        addServicio(new Servicio(null, "Transporte al aeropuerto", "Servicio de traslado desde/hacia el aeropuerto"));
        addServicio(new Servicio(null, "Bar en la playa", "Bar con cócteles y snacks en la playa"));
        addServicio(new Servicio(null, "Club infantil", "Actividades y juegos para niños"));
        addServicio(new Servicio(null, "Eventos y bodas", "Organización de eventos y bodas en el hotel"));
    }

    public List<Servicio> findAll() {
        return new ArrayList<>(servicios.values());
    }

    public Optional<Servicio> findById(Long id) {
        return Optional.ofNullable(servicios.get(id));
    }

    public Servicio addServicio(Servicio servicio) {
        if (servicio.getId() == null) {
            servicio.setId(nextId++);
        }
        servicios.put(servicio.getId(), servicio);
        return servicio;
    }

    public Servicio updateServicio(Long id, Servicio servicio) {
        if (!servicios.containsKey(id)) return null;
        servicio.setId(id);
        servicios.put(id, servicio);
        return servicio;
    }

    public boolean deleteServicio(Long id) {
        return servicios.remove(id) != null;
    }
}
