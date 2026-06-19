package co.edu.usbcali.reservas_suarez.service.impl;

import co.edu.usbcali.reservas_suarez.dto.request.CancelReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.request.CreateReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateReservationAdminRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateReservationClientRequest;
import co.edu.usbcali.reservas_suarez.dto.response.CreateReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.GetPublicReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.GetReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.UpdateReservationResponse;
import co.edu.usbcali.reservas_suarez.exception.ConflictException;
import co.edu.usbcali.reservas_suarez.exception.ResourceNotFoundException;
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
            if (createReservationRequest.getStartDatetime().isAfter(createReservationRequest.getEndDatetime())) {
                throw new RuntimeException("La fecha de inicio no puede ser mayor a la final");
            }

            // Buscar cliente por telefono sino lo crea
            Client client = clientRepository.findByPhone(createReservationRequest.getClientPhone())
                    .orElseGet(() -> {  Client newClient = Client.builder()
                            .name(createReservationRequest.getClientName())
                            .phone(createReservationRequest.getClientPhone())
                            .build();

            return clientRepository
                    .save(newClient);
        });

        //Buscar cancha
        Court court = courtRepository.findById(createReservationRequest.getCourtId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cancha no encontrada"+
                            createReservationRequest.getCourtId()));
        //Buscar status
            ReservationStatus status = reservationStatusRepository.findById(createReservationRequest.getStatusId())
                    .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado" + createReservationRequest.getStatusId()));

            // Covertir a Entity Reservation
            Reservation reservation = Reservation.builder()
                    .client(client)
                    .court(court)
                    .status(status)
                    .startDatetime(createReservationRequest.getStartDatetime())
                    .endDatetime(createReservationRequest.getEndDatetime())
                    .createdBy("CLIENT")
                    .notes(createReservationRequest.getNotes())
                    .createdAt(LocalDateTime.now())
                    .build();

        // Geneera reservation code

        String reservationCode =
                java.util.UUID
                        .randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        reservation.setReservationCode(
                reservationCode
        );


        // Guardar en base de datos
            reservation = reservationRepository.save(reservation);
            // Mapear la entidad a DTO y retormar
            return ReservationMapper.entityToCreateReservationResponse(reservation);


        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("La cancha ya está reservada en ese horario");
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

        //GET ALL PUBLIC
        @Override
        public List<GetPublicReservationResponse> getPublicReservations() {
           List<Reservation> reservations = reservationRepository.findAll();
           return ReservationMapper.entityToListGetPublicReservationResponse(
                        reservations
                );
    }

        //  GET BY ID
        @Override
        public GetReservationResponse getReservationById(Integer id) {

            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Reserva no encontrada con id: " + id));
            return ReservationMapper.entityToGetReservationResponse(reservation);
        }


    // CANCEL
    @Override
    public GetReservationResponse cancelReservation(Integer id, CancelReservationRequest cancelReservationRequest) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada con id: " + id)
                );

        // VALIDAR CÓDIGO

        if (!reservation.getReservationCode().equals(cancelReservationRequest.getReservationCode())) {
            throw new RuntimeException("Código incorrecto");}

        ReservationStatus cancelled = reservationStatusRepository
                        .findById(2)
                        .orElseThrow(() -> new ResourceNotFoundException("Estado CANCELLED no encontrado"));


        reservation.setStatus(cancelled);

        reservation = reservationRepository.save(reservation);

        return ReservationMapper.entityToGetReservationResponse(reservation);
    }

    // UPDATE
    @Override
    public UpdateReservationResponse updateReservationClient (Integer id, UpdateReservationClientRequest updateReservationClientRequest) throws Exception {

        try {

            // Validar request
            if (Objects.isNull(updateReservationClientRequest)) {
                throw new Exception("El objeto UpdateReservationRequest no puede ser nulo");
            }

            // Validar id
            if (Objects.isNull(id) || id <= 0) {
                throw new Exception("El id no puede ser nulo o menor igual a 0");
            }

            // Validar fechas
            if (updateReservationClientRequest.getStartDatetime().isAfter(updateReservationClientRequest.getEndDatetime())) {
                throw new RuntimeException("La fecha inicial no puede ser mayor a la final");
            }


            // Buscar reserva
            Reservation reservation = reservationRepository.findById(id).orElseThrow(() ->
                            new ResourceNotFoundException("No se ha encontrado la reserva con id " + id));


            // VALIDAR RESERVATION CODE
            if (!reservation.getReservationCode().equals(updateReservationClientRequest.getReservationCode())) {
                throw new RuntimeException("Código de reserva incorrecto");}

            // Buscar cancha
            Court court = courtRepository.findById(updateReservationClientRequest.getCourtId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cancha no encontrada"));

            // Actualizar cancha
            reservation.setCourt(court);

            // Actualizar fechas
            reservation.setStartDatetime(
                    updateReservationClientRequest.getStartDatetime()
            );

            reservation.setEndDatetime(
                    updateReservationClientRequest.getEndDatetime()
            );

            //Actualizar notes
            reservation.setNotes(
                    updateReservationClientRequest.getNotes()
            );

            reservation.setCreatedBy("CLIENT");

            // Persistir cambios
            reservation = reservationRepository.save(reservation);

            // Retornar DTO Response
            return ReservationMapper.entityToUpdateReservationResponse(
                    reservation
            );

        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("La cancha ya está reservada en ese horario");
        }
    }

    @Override
    public UpdateReservationResponse updateReservationAdmin(Integer id, UpdateReservationAdminRequest updateReservationAdminRequest) throws Exception {

        try {

            // Validar request
            if (Objects.isNull(updateReservationAdminRequest)) {
                throw new Exception("El objeto UpdateReservationAdminRequest no puede ser nulo");
            }

            // Validar id
            if (Objects.isNull(id) || id <= 0) {
                throw new Exception("El id no puede ser nulo o menor igual a 0");
            }

            // Validar fechas
            if (updateReservationAdminRequest.getStartDatetime().isAfter(updateReservationAdminRequest.getEndDatetime())) {
                throw new RuntimeException("La fecha inicial no puede ser mayor a la final");
            }


            // Buscar reserva
            Reservation reservation = reservationRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException("No se ha encontrado la reserva con id " + id));

            // Buscar cancha
            Court court = courtRepository.findById(updateReservationAdminRequest.getCourtId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cancha no encontrada"));

            // Buscar status
            ReservationStatus status = reservationStatusRepository.findById(updateReservationAdminRequest.getStatusId())
                            .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado"));

            // Actualizar cancha
            reservation.setCourt(court);

            // Actualizar status
            reservation.setStatus(status);

            // Actualizar fechas
            reservation.setStartDatetime(
                    updateReservationAdminRequest.getStartDatetime()
            );

            reservation.setEndDatetime(
                    updateReservationAdminRequest.getEndDatetime()
            );

            //Actualizar notes
            reservation.setNotes(
                    updateReservationAdminRequest.getNotes()
            );


            reservation.setCreatedBy("OWNER");

            // Persistir cambios
            reservation = reservationRepository.save(reservation);

            // Retornar DTO Response
            return ReservationMapper.entityToUpdateReservationResponse(
                    reservation
            );

        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("La cancha ya está reservada en ese horario");
        }
    }


    // DELETE
    @Override
    public void deleteReservation (Integer id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada con id: " + id)
                );

        reservationRepository.delete(reservation);
    }
}