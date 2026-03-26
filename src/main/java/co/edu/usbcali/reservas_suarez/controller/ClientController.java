package co.edu.usbcali.reservas_suarez.controller;

import co.edu.usbcali.reservas_suarez.dto.response.GetClientResponse;
import co.edu.usbcali.reservas_suarez.mapper.ClientMapper;
import co.edu.usbcali.reservas_suarez.model.Client;
import co.edu.usbcali.reservas_suarez.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/clients")



public class ClientController {
    //Inyeccion de dependencias
    private final ClientRepository clientRepository;

    @GetMapping("/ping")
    public String ping (){
        return "pong";
    }

    @GetMapping("/all")
    public List<GetClientResponse> getAllClients() {
        //Declarar nueva lista de clientResponse
        List<GetClientResponse> clientsResponse;

        //Ir al Repository y obtener todos los usuarios
        List<Client> clients =clientRepository.findAll();

        //Convertir la lista de clientes a lista clientResponse
        clientsResponse= ClientMapper.entityToListGetClientResponse(clients);
        return clientsResponse;

    }

    @GetMapping("/{id}")
    public ResponseEntity<GetClientResponse> getClientById(@PathVariable Integer id){
    Client client= clientRepository.getReferenceById(id);
    GetClientResponse clientResponse = ClientMapper.entityToGetClientResponse(client);

        return new ResponseEntity<> (
                clientResponse,
            HttpStatus.OK
        );
    }
}
