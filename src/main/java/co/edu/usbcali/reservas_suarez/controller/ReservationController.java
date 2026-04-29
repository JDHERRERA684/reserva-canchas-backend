package co.edu.usbcali.reservas_suarez.controller;

import co.edu.usbcali.reservas_suarez.dto.request.CreateReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.response.GetReservationResponse;
import co.edu.usbcali.reservas_suarez.mapper.ReservationMapper;
import co.edu.usbcali.reservas_suarez.model.Reservation;
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

    private final ReservationService reservationService;


    @GetMapping("/ping")
    public String ping (){
        return "pong";
    }


    @GetMapping("/all")
    public List<GetReservationResponse> getAllReservations() {
        return reservationService.getAllReservations();
    }


    @GetMapping("/{id}")
    public ResponseEntity<GetReservationResponse> getReservationById(
            @PathVariable Integer id){

        GetReservationResponse reservationResponse =
                reservationService.getReservationById(id);

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

    //En mi caso se maneja por states donde 2 es cancel
    @PutMapping("/cancel/{id}")
    public ResponseEntity<GetReservationResponse> cancelReservation(@PathVariable Integer id) {

        GetReservationResponse reservationUpdated =
                reservationService.cancelReservation(id);

        return new ResponseEntity<>(
                reservationUpdated,
                HttpStatus.OK
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GetReservationResponse> updateReservation( @PathVariable Integer id,
            @RequestBody CreateReservationRequest createReservationRequest
    ) throws Exception {

        GetReservationResponse reservationUpdated =
                reservationService.updateReservation(id, createReservationRequest);

        return new ResponseEntity<>(
                reservationUpdated,
                HttpStatus.OK
        );
    }

}
