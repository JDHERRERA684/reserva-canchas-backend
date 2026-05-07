package co.edu.usbcali.reservas_suarez.service.impl;

import co.edu.usbcali.reservas_suarez.dto.request.CreateReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.response.CreateReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.GetReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.UpdateReservationResponse;
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
    public CreateReservationResponse createReservation(CreateReservationRequest createReservationRequest) throws Exception {
        try {
            if (Objects.isNull(createReservationRequest)) {
                throw new Exception("El objeto CreateReservationRequest no puede ser nulo");
            }

            if (Objects.isNull(createReservationRequest.getClientId()) || createReservationRequest.getClientId() <= 0)  {
                throw new Exception("El clientId es requerido");
            }

            if (Objects.isNull(createReservationRequest.getCourtId())|| createReservationRequest.getCourtId() <= 0) {
                throw new Exception("El courtId es requerido");
            }

            if (Objects.isNull(createReservationRequest.getStatusId())|| createReservationRequest.getStatusId() <= 0) {
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
            reservation = reservationRepository.save(reservation);
            // Mapear la entidad a DTO y retormar
            return ReservationMapper.entityToCreateReservationResponse(reservation);


        } catch (DataIntegrityViolationException e) {
            throw new Exception("La cancha ya está reservada en ese horario");
        }
    }

        // GET ALL
        @Override
        public List<GetReservationResponse> getAllReservations() {
            List<Reservation> reservations = reservationRepository.findAll();
            return ReservationMapper.entityToListGetReservationResponse(
                    reservations
            );
        }

        //  GET BY ID
        @Override
        public GetReservationResponse getReservationById(Integer id) {

            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Reserva no encontrada con id: " + id));
            return ReservationMapper.entityToGetReservationResponse(reservation);
        }


    // CANCEL (ESE VA CON EL STATUS)
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

    // UPDATE
    @Override
    public UpdateReservationResponse updateReservation(Integer id, UpdateReservationRequest updateReservationRequest) throws Exception {

        try {

            // Validar request
            if (Objects.isNull(updateReservationRequest)) {
                throw new Exception("El objeto UpdateReservationRequest no puede ser nulo");
            }

            // Validar id
            if (Objects.isNull(id) || id <= 0) {
                throw new Exception("El id no puede ser nulo o menor igual a 0");
            }

            // Validar fechas
            if (Objects.isNull(updateReservationRequest.getStartDatetime())) {
                throw new Exception("La fecha inicial no puede ser nula");
            }

            if (Objects.isNull(updateReservationRequest.getEndDatetime())) {
                throw new Exception("La fecha final no puede ser nula");
            }

            // Buscar reserva
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() ->
                            new Exception(
                                    "No se ha encontrado la reserva con id " + id
                            )
                    );

            // Actualizar datos
            reservation.setStartDatetime(
                    updateReservationRequest.getStartDatetime()
            );

            reservation.setEndDatetime(
                    updateReservationRequest.getEndDatetime()
            );

            reservation.setNotes(
                    updateReservationRequest.getNotes()
            );

            // Persistir cambios
            reservation = reservationRepository.save(reservation);

            // Retornar DTO Response
            return ReservationMapper.entityToUpdateReservationResponse(
                    reservation
            );

        } catch (DataIntegrityViolationException e) {
            throw new Exception("La cancha ya está reservada en ese horario");
        }
    }


    // DELETE
    @Override
    public void deleteReservation (Integer id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Reserva no encontrada con id: " + id)
                );

        reservationRepository.delete(reservation);
    }
}