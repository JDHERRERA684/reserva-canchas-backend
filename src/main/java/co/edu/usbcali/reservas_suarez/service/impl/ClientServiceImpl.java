package co.edu.usbcali.reservas_suarez.service.impl;

import co.edu.usbcali.reservas_suarez.dto.request.CreateClientRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateClientRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetClientResponse;
import co.edu.usbcali.reservas_suarez.dto.response.UpdateClientResponse;
import co.edu.usbcali.reservas_suarez.mapper.ClientMapper;
import co.edu.usbcali.reservas_suarez.model.Client;
import co.edu.usbcali.reservas_suarez.repository.ClientRepository;
import co.edu.usbcali.reservas_suarez.service.ClientService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor

public class ClientServiceImpl implements ClientService {

    //Inyeccion de dependencias (Repository
    private final ClientRepository clientRepository;

    // CREATE
    @Override
    public GetClientResponse createClient(CreateClientRequest createClientRequest) throws Exception {

         try {
             //Validar cada campo

             if (Objects.isNull(createClientRequest)) {
                 throw new Exception("El objeto CreateClientRequest no puede ser nulo");
             }
             if (Objects.isNull(createClientRequest.getName()) || createClientRequest.getName().isBlank()) {
                 throw new Exception("El nombre es requerido");
             }
             if (createClientRequest.getName().length() > 100) {
                 throw new Exception("El nombre solo soporta hasta 100 caracteres ");
             }

             if (Objects.isNull(createClientRequest.getPhone()) || createClientRequest.getPhone().isBlank()) {
                 throw new Exception("El telefono es requerido");
             }
             if (createClientRequest.getPhone().length() > 20) {
                 throw new Exception("El numero solo soporta hasta 20 caracteres ");
             }

            //Converit desde el Request hacia la Entidad usando el Mapper
            Client client = ClientMapper.createClientRequestToEntity(createClientRequest);
            //Guardar el cliente (entidad) usando el Respository
            client = clientRepository.save(client);

            //Retonar el Dto Response
            return ClientMapper.entityToGetClientResponse(client);
        } catch (Exception e) {
             throw e;
         }

    }

    // GET ALL
    @Override
    public List<GetClientResponse> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return ClientMapper.entityToListGetClientResponse(clients);
    }

     //GET BY ID
    @Override
    public GetClientResponse getClientById(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cliente no encontrado con id: " + id)
                );
        return ClientMapper.entityToGetClientResponse(client);
    }

    // UPDATE
    @Override
    public UpdateClientResponse updateClient(Integer id, UpdateClientRequest updateClientRequest) throws Exception {

        try {
        // Validar objeto request
        if (Objects.isNull(updateClientRequest)) {
            throw new Exception("El updateClientRequest no puede ser nulo");
        }
        // Buscar cliente
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("No se ha encontrado el cliente con id " + id)
                );
        // Actualizar name
        if (Objects.nonNull(updateClientRequest.getName()) && !updateClientRequest.getName().isBlank()) {
            if (updateClientRequest.getName().length() > 100) {
                throw new Exception("El nombre solo soporta hasta 100 caracteres");
            }
            client.setName(updateClientRequest.getName());
        }
        // Actualizar phone
        if (Objects.nonNull(updateClientRequest.getPhone()) && !updateClientRequest.getPhone().isBlank()) {
            if (updateClientRequest.getPhone().length() > 20) {
                throw new Exception("El teléfono solo soporta hasta 20 caracteres");
            }
            client.setPhone(updateClientRequest.getPhone());
        }

        client = clientRepository.save(client);

        return ClientMapper.entityToUpdateClientResponse(client);
        } catch (Exception e) {
            throw e;
        }
    }

    // DELETE
    @Override
    public void deleteClient(Integer id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cliente no encontrado con id: " + id)
                );

        clientRepository.delete(client);
    }
}

