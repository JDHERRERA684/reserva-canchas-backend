package co.edu.usbcali.reservas_suarez.service;

import co.edu.usbcali.reservas_suarez.dto.request.CreateReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.response.CreateReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.GetReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.UpdateReservationResponse;


import java.util.List;

public interface ReservationService {
    CreateReservationResponse createReservation(CreateReservationRequest createReservationRequest) throws Exception;
    List<GetReservationResponse> getAllReservations();
    GetReservationResponse getReservationById(Integer id);
    UpdateReservationResponse updateReservation(Integer id, UpdateReservationRequest updateReservationRequest) throws Exception;
    GetReservationResponse cancelReservation(Integer id);
    void deleteReservation(Integer id);

}
