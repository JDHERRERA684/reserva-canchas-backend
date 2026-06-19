package co.edu.usbcali.reservas_suarez.service;

import co.edu.usbcali.reservas_suarez.dto.request.CreateReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateReservationAdminRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateReservationClientRequest;
import co.edu.usbcali.reservas_suarez.dto.response.CreateReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.GetPublicReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.GetReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.UpdateReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.request.CancelReservationRequest;


import java.util.List;

public interface ReservationService {
    CreateReservationResponse createReservation(CreateReservationRequest createReservationRequest) throws Exception;
    List<GetReservationResponse> getAllReservations();
    List<GetPublicReservationResponse> getPublicReservations();
    GetReservationResponse getReservationById(Integer id);
    UpdateReservationResponse updateReservationClient (Integer id, UpdateReservationClientRequest updateReservationClientRequest) throws Exception;
    UpdateReservationResponse updateReservationAdmin(Integer id, UpdateReservationAdminRequest updateReservationAdminRequest) throws Exception;
    GetReservationResponse cancelReservation(Integer id, CancelReservationRequest cancelReservationRequest);
    void deleteReservation(Integer id);

}
