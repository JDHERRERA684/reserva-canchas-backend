package co.edu.usbcali.reservas_suarez.service.impl;

import co.edu.usbcali.reservas_suarez.dto.request.CreateClientRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetClientResponse;
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

            //Validar el objeto CreateClientRequest y todos sus atributos
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
        }

    // GET ALL
    @Override
    public List<GetClientResponse> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        List<GetClientResponse> getClientResponseList =
                ClientMapper.entityToListGetClientResponse(clients);
        return getClientResponseList;
    }

     //GET BY ID
    @Override
    public GetClientResponse getClientById(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cliente no encontrado con id: " + id)
                );
        GetClientResponse getClientResponse =
                ClientMapper.entityToGetClientResponse(client);
        return getClientResponse;
    }
    // UPDATE
    @Override
    public GetClientResponse updateClient(Integer id, CreateClientRequest createClientRequest) throws Exception {

        if (Objects.isNull(createClientRequest)) {
            throw new Exception("El request no puede ser nulo");
        }

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cliente no encontrado con id: " + id)
                );

        if (createClientRequest.getName() != null && !createClientRequest.getName().isBlank()) {
            if (createClientRequest.getName().length() > 100) {
                throw new Exception("El nombre solo soporta hasta 100 caracteres");
            }
            client.setName(createClientRequest.getName());
        }

        if (createClientRequest.getPhone() != null && !createClientRequest.getPhone().isBlank()) {
            if (createClientRequest.getPhone().length() > 20) {
                throw new Exception("El teléfono solo soporta hasta 20 caracteres");
            }
            client.setPhone(createClientRequest.getPhone());
        }

        client = clientRepository.save(client);

        return ClientMapper.entityToGetClientResponse(client);
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

