package co.edu.usbcali.reservas_suarez.controller;

import co.edu.usbcali.reservas_suarez.dto.request.CreateReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetReservationResponse;
import co.edu.usbcali.reservas_suarez.mapper.ReservationMapper;
import co.edu.usbcali.reservas_suarez.model.Reservation;
import co.edu.usbcali.reservas_suarez.repository.ReservationRepository;
import co.edu.usbcali.reservas_suarez.service.ReservationService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/reservations")

public class ReservationController {

    // Inyección de dependencias
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;


    @GetMapping("/ping")
    public String ping (){
        return "pong";
    }


    @GetMapping("/all")
    public List<GetReservationResponse> getAllReservations() {

        // Declarar lista response
        List<GetReservationResponse> reservationsResponse;

        // Obtener todas las reservas
        List<Reservation> reservations = reservationRepository.findAll();

        // Convertir a response
        reservationsResponse =
                ReservationMapper.entityToListGetReservationResponse(reservations);

        return reservationsResponse;
    }


    @GetMapping("/{id}")
    public ResponseEntity<GetReservationResponse> getReservationById(
            @PathVariable Integer id){

        Reservation reservation = reservationRepository.getReferenceById(id);

        GetReservationResponse reservationResponse =
                ReservationMapper.entityToGetReservationResponse(reservation);

        return new ResponseEntity<>(
                reservationResponse,
                HttpStatus.OK
        );
    }


    @PostMapping("/create")
    public ResponseEntity<GetReservationResponse> createReservation(
            @RequestBody CreateReservationRequest createReservationRequest
    ) throws Exception {

        GetReservationResponse reservationCreated =
                reservationService.createReservation(createReservationRequest);

        return new ResponseEntity<>(
                reservationCreated,
                HttpStatus.CREATED
        );
    }

}
