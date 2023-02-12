package com.example.exercise.infrastructure.db.springdata.controllers;

import com.example.exercise.infrastructure.db.springdata.dtos.ClientDto;
import com.example.exercise.infrastructure.db.springdata.model.Client;
import com.example.exercise.infrastructure.db.springdata.repositories.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ClientController {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ClientDto getById(Long id) {
        Client client = clientRepository.findById(id).orElse(null);
        return convertToDto(client);
    }

    private ClientDto convertToDto(Client client) {
        ClientDto clientDto = modelMapper.map(client, ClientDto.class);
        return clientDto;
    }

    private Client convertToEntity(ClientDto clientDto) {
        Client client = modelMapper.map(clientDto, Client.class);
        return client;
    }

}
