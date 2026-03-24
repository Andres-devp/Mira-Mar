package com.example.demo.service;

import com.example.demo.entities.Client;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Client> getAllClientes() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Client getClienteById(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró cliente con ID: " + id, id));
    }

    @Override
    public Client saveCliente(Client cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void deleteCliente(Long id) {
        Client cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró cliente con ID: " + id, id));
        clienteRepository.delete(cliente);
    }
}
