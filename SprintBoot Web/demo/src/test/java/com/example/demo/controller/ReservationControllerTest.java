package com.example.demo.controller;

import com.example.demo.controller.dto.CreateReservationRequest;
import com.example.demo.entities.Reservation;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void ReservationController_getAllReservas_Reservations() throws Exception {
        // Arrange
        Reservation res1 = new Reservation();
        res1.setId(1L);
        Reservation res2 = new Reservation();
        res2.setId(2L);
        List<Reservation> list = Arrays.asList(res1, res2);

        when(reservationService.getAllReservas()).thenReturn(list);

        // Act  Assert
        
        ResultActions response = mockMvc.perform(get("/reservations/all"));

                response.andExpect(status().isOk())
                        .andExpect(jsonPath("$.size()").value(2))
                        .andExpect(jsonPath("$[0].id").value(1))
                        .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void ReservationController_getReservaById_Reservation() throws Exception {
        // Arrange
        Reservation res = new Reservation();
        res.setId(1L);

        when(reservationService.getReservaById(1L)).thenReturn(res);

        // Act 
        ResultActions response = mockMvc.perform(get("/reservations/1"));


        // Assert
        response.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
                
    }



    @Test
    public void ReservationController_saveReserva_Reservation() throws Exception {
        // Arrange
        Reservation updateRequest = new Reservation();
        updateRequest.setId(1L);

        Reservation updatedReservation = new Reservation();
        updatedReservation.setId(1L);

        when(reservationService.saveReserva(any(Reservation.class))).thenReturn(updatedReservation);

        // Act
        ResultActions response = mockMvc.perform(put("/reservations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void ReservationController_getReservaById_NotFound() throws Exception {
        // Arrange
        when(reservationService.getReservaById(99L))
                .thenThrow(new NotFoundException("No se encontró reserva con ID: 99", 99L));

        // Act
        ResultActions response = mockMvc.perform(get("/reservations/99"));

        // Assert
        response.andExpect(status().isNotFound());
    }
}