package com.example.demo.service;

import com.example.demo.entities.Operator;

import java.util.List;

public interface OperatorService {

    List<Operator> getAllOperadores();

    Operator getOperadorById(Long id);

    Operator saveOperador(Operator operador);

    void deleteOperador(Long id);
}
