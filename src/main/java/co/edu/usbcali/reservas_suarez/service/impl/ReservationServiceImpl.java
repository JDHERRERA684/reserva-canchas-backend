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
import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final CourtRepository courtRepository;
    private final ReservationStatusRepository reservationStatusRepository;

    // CREATE
    @Override
    public GetReservationResponse createReservation(CreateReservationRequest createReservationRequest) throws Exception {

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

            ReservationStatus status = reservationStatusRepository.findById(createReservationRequest.getStatusId())
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

    }

        // GET ALL
        @Override
        public List<GetReservationResponse> getAllReservations() {
            List<Reservation> reservations = reservationRepository.findAll();
            List<GetReservationResponse> getReservationResponseList = ReservationMapper.entityToListGetReservationResponse(reservations);
            return getReservationResponseList;
    }

        //  GET BY ID
        @Override
        public GetReservationResponse getReservationById(Integer id) {

            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Reserva no encontrada con id: " + id));
            GetReservationResponse getReservationResponse = ReservationMapper.entityToGetReservationResponse(reservation);
            return  getReservationResponse;
        }

    // CANCEL (PUT)
    @Override
    public GetReservationResponse cancelReservation(Integer id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Reserva no encontrada con id: " + id)
                );

        ReservationStatus cancelled = reservationStatusRepository.findById(2)
                .orElseThrow(() ->
                        new RuntimeException("Estado CANCELLED no encontrado")
                );

        reservation.setStatus(cancelled);

        reservation = reservationRepository.save(reservation);

        return ReservationMapper.entityToGetReservationResponse(reservation);
    }

    // UPDATE (PUT)
    @Override
    public GetReservationResponse updateReservation(Integer id, CreateReservationRequest request) throws Exception {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Reserva no encontrada con id: " + id)
                );

        reservation.setStartDatetime(request.getStartDatetime());
        reservation.setEndDatetime(request.getEndDatetime());
        reservation.setNotes(request.getNotes());

        try {
            reservation = reservationRepository.save(reservation);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("La cancha ya está reservada en ese horario");
        }

        return ReservationMapper.entityToGetReservationResponse(reservation);
    }
}