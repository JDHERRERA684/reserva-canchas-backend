package co.edu.usbcali.reservas_suarez.service;

import co.edu.usbcali.reservas_suarez.dto.request.CreateClientRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateClientRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetClientResponse;
import co.edu.usbcali.reservas_suarez.dto.response.UpdateClientResponse;

import java.util.List;

public interface ClientService {

  GetClientResponse createClient(CreateClientRequest createClientRequest) throws Exception;
  List<GetClientResponse> getAllClients();
  GetClientResponse getClientById(Integer id);
  UpdateClientResponse updateClient(Integer id, UpdateClientRequest updateClientRequest) throws Exception;
  void deleteClient(Integer id);
}