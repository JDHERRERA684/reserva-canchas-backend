package co.edu.usbcali.reservas_suarez.service;

import co.edu.usbcali.reservas_suarez.dto.request.CreateClientRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetClientResponse;
import java.util.List;

public interface ClientService {

  GetClientResponse createClient(CreateClientRequest createClientRequest) throws Exception;
  List<GetClientResponse> getAllClients();
  GetClientResponse getClientById(Integer id);
  GetClientResponse updateClient(Integer id, CreateClientRequest request) throws Exception;
  void deleteClient(Integer id);
}
