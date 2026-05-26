package com.example.demo.service;

import com.example.demo.entities.Operator;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OperatorServiceImpl implements OperatorService {

    @Autowired
    private OperatorRepository operadorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Operator> getAllOperadores() {
        return operadorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Operator getOperadorById(Long id) {
        return operadorRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró operador con ID: " + id, id));
    }

    @Override
    public Operator saveOperador(Operator operador) {
        if (operador.getId() != null) {
            String incoming = operador.getContrasena();
            if (incoming == null || incoming.isBlank()) {
                operadorRepository.findById(operador.getId()).ifPresent(existing ->
                    operador.setContrasena(existing.getContrasena())
                );
            }
        }
        return operadorRepository.save(operador);
    }

    @Override
    public void deleteOperador(Long id) {
        Operator operador = operadorRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró operador con ID: " + id, id));
        operadorRepository.delete(operador);
    }
}
