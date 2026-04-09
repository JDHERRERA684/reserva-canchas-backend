package co.edu.usbcali.reservas_suarez.mapper;
import co.edu.usbcali.reservas_suarez.dto.request.CreateClientRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetClientResponse;
import co.edu.usbcali.reservas_suarez.model.Client;

import java.util.ArrayList;
import java.util.List;




public class ClientMapper {
    public static GetClientResponse entityToGetClientResponse(Client client){
        //Instanciar nuevo objeto GetUserResponse
        GetClientResponse getClientResponse = GetClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .phone(client.getPhone())
                .createdAt(client.getCreatedAt())
                .build();

        return getClientResponse;
    }

    public static List<GetClientResponse> entityToListGetClientResponse(List<Client> clients){
        /* //Instaciar lista de DTO GetClientResponse vacia inicialmente
        List<GetClientResponse> getClientResponseList = new ArrayList<>();

        //Iterar sobre la lista de objetos Client y agregarlos a la lista de objetos GetClientResponse
        for(int i = 0; i < clients.size(); i++){

            //Por cada iteracion de la lista, obtener el objeto Client actual y convertirlo a DTO GetClientResponse
            Client client = clients.get(i);
            GetClientResponse getClientResponse = entityToGetClientResponse(client);

            //Agregar el objeto DTO GetClientResponse a la lista de objetos GetClientResponse
            getClientResponseList.add(getClientResponse);
        }

        //Retornar la lista de DTO GetClientResponse
        return getClientResponseList;*/
        return clients.stream().map(ClientMapper::entityToGetClientResponse).toList();
    }

    public static Client createClientRequestToEntity(CreateClientRequest createClientRequest){
        return Client.builder()
                .name(createClientRequest.getName())
                .phone(createClientRequest.getPhone())
                .build();
    }
}
