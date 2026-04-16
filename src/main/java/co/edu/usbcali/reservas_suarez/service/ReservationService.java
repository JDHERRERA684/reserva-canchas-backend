package co.edu.usbcali.reservas_suarez.service;

import co.edu.usbcali.reservas_suarez.dto.request.CreateReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetReservationResponse;

public interface ReservationService {

    GetReservationResponse createReservation(CreateReservationRequest createReservationRequest) throws Exception;

}
