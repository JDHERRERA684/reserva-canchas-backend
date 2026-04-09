package co.edu.usbcali.reservas_suarez.service;

import co.edu.usbcali.reservas_suarez.dto.request.CreateClientRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetClientResponse;

public interface ClientService {

  GetClientResponse createClient(CreateClientRequest createClientRequest) throws Exception;
}
