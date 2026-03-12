package com.example.demo.service;

import com.example.demo.entities.Cliente;

import java.util.List;

public interface ClienteService {

    List<Cliente> getAllClientes();

    Cliente getClienteById(Long id);

    Cliente saveCliente(Cliente cliente);

    void deleteCliente(Long id);
}
