package com.example.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;

import com.example.demo.repository.AccountItemRepository;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.HotelServiceRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.RoomTypeRepository;
import com.example.demo.service.ReservationServiceImpl;

@SpringBootTest
@ActiveProfiles("test")
@DirtiestContext(classMode = DirtiestContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservaServiceMoquitoTest {



}
