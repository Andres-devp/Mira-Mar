package com.example.demo.service;

import com.example.demo.entities.Client;

import java.util.List;

public interface ClientService {

    List<Client> getAllClientes();

    Client getClienteById(Long id);

    Client saveCliente(Client cliente);

    Client updateCliente(Long id, Client datos);

    void deleteCliente(Long id);

    void eliminarCuenta(Long id);
}
