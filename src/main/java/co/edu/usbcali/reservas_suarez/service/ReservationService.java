package co.edu.usbcali.reservas_suarez.service;

import co.edu.usbcali.reservas_suarez.dto.request.CreateReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetReservationResponse;

import java.util.List;

public interface ReservationService {

    GetReservationResponse createReservation(CreateReservationRequest createReservationRequest) throws Exception;
    List<GetReservationResponse> getAllReservations();
    GetReservationResponse getReservationById(Integer id);
    GetReservationResponse cancelReservation(Integer id);
    GetReservationResponse updateReservation(Integer id, CreateReservationRequest request) throws Exception;
}
