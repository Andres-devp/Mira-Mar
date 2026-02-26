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
        addServicio(new Servicio("https://i.pinimg.com/736x/49/be/79/49be795193f4d6bd20b7a7d1dbc644f3.jpg", "Restaurante", "Restaurante gourmet con menú internacional"));
        addServicio(new Servicio("https://mojosurf.es/wp-content/uploads/2024/12/Clases-de-surf-que-tener-en-cuenta.jpg", "Clases de surf", "Clases de surf para todos los niveles"));
        addServicio(new Servicio("https://i.pinimg.com/736x/17/95/d9/1795d9b9e9734035ea365debecc48267.jpg", "Caminatas guiadas", "Excursiones ygit  caminatas por la naturaleza"));
        addServicio(new Servicio("https://i.pinimg.com/736x/91/9a/fc/919afcf0663bf853bf584e8672166dd0.jpg", "Spa & Wellness", "Masajes, sauna y tratamientos de spa"));
        addServicio(new Servicio("https://i.pinimg.com/736x/54/26/0d/54260d946194dd1ac4a500cda97194ad.jpg", "Alquiler de bicicletas", "Bicicletas para recorrer la zona"));
        addServicio(new Servicio("https://i.pinimg.com/736x/04/35/9b/04359b99919a8debaaba2173b988927d.jpg", "Piscina", "Piscina exterior con bar y solárium"));
        addServicio(new Servicio("https://i.pinimg.com/736x/1d/1c/95/1d1c9548a787dbf89974fc4e957d5a13.jpg", "Transporte al aeropuerto", "Servicio de traslado desde/hacia el aeropuerto"));
        addServicio(new Servicio("https://i.pinimg.com/736x/5c/c3/d4/5cc3d4c5021bdaf8ebdcbfdf33f4757f.jpg", "Bar en la playa", "Bar con cócteles y snacks en la playa"));
        addServicio(new Servicio("https://i.pinimg.com/736x/02/46/41/024641a99c2aada4e851b2ebd80e3a13.jpg", "Club infantil", "Actividades y juegos para niños"));
        addServicio(new Servicio("https://i.pinimg.com/736x/4e/2a/90/4e2a90524e644f8f95784f3b805d06ae.jpg", "Eventos y bodas", "Organización de eventos y bodas en el hotel"));
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
