package co.edu.usbcali.reservas_suarez.service.impl;

import co.edu.usbcali.reservas_suarez.dto.request.CreateReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetReservationResponse;
import co.edu.usbcali.reservas_suarez.mapper.ReservationMapper;
import co.edu.usbcali.reservas_suarez.model.*;
import co.edu.usbcali.reservas_suarez.repository.*;
import co.edu.usbcali.reservas_suarez.service.ReservationService;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;


@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final CourtRepository courtRepository;
    private final ReservationStatusRepository reservationStatusRepository;

    @Override
    public GetReservationResponse createReservation(CreateReservationRequest createReservationRequest) throws Exception {

        try {

            if (Objects.isNull(createReservationRequest)) {
                throw new Exception("El objeto CreateReservationRequest no puede ser nulo");
            }

            if (Objects.isNull(createReservationRequest.getClientId())) {
                throw new Exception("El clientId es requerido");
            }

            if (Objects.isNull(createReservationRequest.getCourtId())) {
                throw new Exception("El courtId es requerido");
            }

            if (Objects.isNull(createReservationRequest.getStatusId())) {
                throw new Exception("El statusId es requerido");
            }

            if (Objects.isNull(createReservationRequest.getStartDatetime()) ||
                    Objects.isNull(createReservationRequest.getEndDatetime())) {
                throw new Exception("Las fechas son obligatorias");
            }

            if (createReservationRequest.getStartDatetime().isAfter(createReservationRequest.getEndDatetime())) {
                throw new Exception("La fecha de inicio no puede ser mayor a la final");
            }

            if (Objects.isNull(createReservationRequest.getCreatedBy()) ||
                    createReservationRequest.getCreatedBy().isBlank()) {
                throw new Exception("El campo createdBy es requerido");
            }

            // Buscar entidad
            Client client = clientRepository.findById(createReservationRequest.getClientId())
                    .orElseThrow(() -> new Exception("Cliente no encontrado"+
                            createReservationRequest.getClientId()));

            Court court = courtRepository.findById(createReservationRequest.getCourtId())
                    .orElseThrow(() -> new Exception("Cancha no encontrada"+
                            createReservationRequest.getCourtId()));

            ReservationStatu status = reservationStatusRepository.findById(createReservationRequest.getStatusId())
                    .orElseThrow(() -> new Exception("Estado no encontrado" + createReservationRequest.getStatusId()));

            // Covertir a Entity Reservation
            Reservation reservation = Reservation.builder()
                    .client(client)
                    .court(court)
                    .status(status)
                    .startDatetime(createReservationRequest.getStartDatetime())
                    .endDatetime(createReservationRequest.getEndDatetime())
                    .createdBy(createReservationRequest.getCreatedBy())
                    .notes(createReservationRequest.getNotes())
                    .createdAt(LocalDateTime.now())
                    .build();

            // Guardar en base de datos
            try {
                reservation = reservationRepository.save(reservation);
            } catch (DataIntegrityViolationException e) {
                throw new Exception("La cancha ya está reservada en ese horario");
            }

            // Mapear la entidad a DTO y retormar
            return ReservationMapper.entityToGetReservationResponse(reservation);

        } catch (Exception e) {
            throw e;
        }
    }
}