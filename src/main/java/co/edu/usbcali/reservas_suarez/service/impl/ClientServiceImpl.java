package co.edu.usbcali.reservas_suarez.service.impl;

import co.edu.usbcali.reservas_suarez.dto.request.CreateClientRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetClientResponse;
import co.edu.usbcali.reservas_suarez.mapper.ClientMapper;
import co.edu.usbcali.reservas_suarez.model.Client;
import co.edu.usbcali.reservas_suarez.repository.ClientRepository;
import co.edu.usbcali.reservas_suarez.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class ClientServiceImpl implements ClientService {

    //Inyeccion de dependencias (Repository
    private final ClientRepository clientRepository;

    @Override
    public GetClientResponse createClient(CreateClientRequest createClientRequest) throws Exception {
        try{
            //Validar el objeto CreateClientRequest y todos sus atributos
         if (createClientRequest == null){
             throw new Exception ("El objeto CreateClientRequest no puede ser nulo");
         }
         if (createClientRequest.getName()==null || createClientRequest.getName().isBlank()){
         throw new Exception ("El nombre es requerido");
         }
         if (createClientRequest.getName().length()>100){
             throw new Exception ("El nombre solo soporta hasta 100 caracteres ");
         }

            if (createClientRequest.getPhone()==null || createClientRequest.getPhone().isBlank()){
                throw new Exception ("El telefono es requerido");
            }
            if (createClientRequest.getPhone().length()>20){
                throw new Exception ("El numero solo soporta hasta 20 caracteres ");
            }

         //Converit desde el REquest hacia la Entidad usando el Mapper
         Client client = ClientMapper.createClientRequestToEntity(createClientRequest);
        //Guardar el cliente (entidad) usando el Respository
        client = clientRepository.save (client);

        //Mapear la entidad a DTO response
            GetClientResponse getClientResponse = ClientMapper.entityToGetClientResponse(client);
         //Retonar el Dto Response
         return getClientResponse;
         }catch (Exception e){
            throw e;
        }

    }
}
