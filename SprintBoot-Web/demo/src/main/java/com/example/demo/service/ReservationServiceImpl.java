package com.example.demo.service;

import com.example.demo.entities.Reservation;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservas() {
        return reservaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation getReservaById(Long id) {
        return reservaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontro reserva con ID: " + id, id));
    }

    @Override
    public Reservation saveReserva(Reservation reserva) {
        return reservaRepository.save(reserva);
    }

    @Override
    @Transactional
    public void deleteReserva(Long id) {
        Reservation reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontro reserva con ID: " + id, id));
        reservaRepository.delete(reserva);
    }
}
