package co.edu.usbcali.reservas_suarez.controller;

import co.edu.usbcali.reservas_suarez.dto.request.CreateClientRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateClientRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetClientResponse;
import co.edu.usbcali.reservas_suarez.dto.response.UpdateClientResponse;
import co.edu.usbcali.reservas_suarez.mapper.ClientMapper;
import co.edu.usbcali.reservas_suarez.model.Client;
import co.edu.usbcali.reservas_suarez.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/clients")



public class ClientController {
    //Inyeccion de dependencias

    private final ClientService clientService;

    // @GetMapping("/ping")
    // public String ping (){
    //    return "pong";
    //}

    @GetMapping("/all")
    public List<GetClientResponse> getAllClients() {
        //Declarar nueva lista de clientResponse
        //List<GetClientResponse> clientsResponse;
        return clientService.getAllClients();

    }

    @GetMapping("/{id}")
    public ResponseEntity<GetClientResponse> getClientById(@PathVariable Integer id){
        GetClientResponse clientResponse = clientService.getClientById(id);
        return new ResponseEntity<> (
            clientResponse,
            HttpStatus.OK
        );
    }

    @PostMapping("/create")
    public ResponseEntity<GetClientResponse> createClient(@Valid @RequestBody CreateClientRequest createClientRequest
    ) throws Exception {

        GetClientResponse clientCreated =
                clientService.createClient(createClientRequest);

        return new ResponseEntity<>(
                clientCreated,
                HttpStatus.CREATED
        );
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateClientResponse> updateClient(@PathVariable Integer id,
            @Valid @RequestBody UpdateClientRequest updateClientRequest) throws Exception {
        UpdateClientResponse clientUpdated = clientService.updateClient(id, updateClientRequest);

        return new ResponseEntity<>(
                clientUpdated,
                HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Integer id){
        clientService.deleteClient(id);
        return new ResponseEntity<>(
                "Cliente eliminado",
                HttpStatus.OK);
    }
}



