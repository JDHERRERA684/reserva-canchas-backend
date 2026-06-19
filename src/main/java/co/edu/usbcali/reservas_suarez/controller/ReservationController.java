package co.edu.usbcali.reservas_suarez.controller;

import co.edu.usbcali.reservas_suarez.dto.request.CancelReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.request.CreateReservationRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateReservationClientRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateReservationAdminRequest;
import co.edu.usbcali.reservas_suarez.dto.response.CreateReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.GetPublicReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.GetReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.UpdateReservationResponse;
import co.edu.usbcali.reservas_suarez.service.ReservationService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/reservations")

public class ReservationController {

    // Inyección de dependencias

    private final ReservationService reservationService;


    //@GetMapping("/ping")
    //public String ping (){
    //    return "pong";
    //}


    @GetMapping("/all")
    public List<GetReservationResponse> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/public")
    public List<GetPublicReservationResponse> getPublicReservations() {

        return reservationService.getPublicReservations();
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
    public ResponseEntity<CreateReservationResponse> createReservation(
            @Valid @RequestBody CreateReservationRequest createReservationRequest
    ) throws Exception {

        CreateReservationResponse reservationCreated =
                reservationService.createReservation(createReservationRequest);

        return new ResponseEntity<>(
                reservationCreated,
                HttpStatus.CREATED
        );
    }

    //CANCEL
    @PutMapping("/cancel/{id}")
    public ResponseEntity<GetReservationResponse> cancelReservation(@PathVariable Integer id,  @Valid @RequestBody
    CancelReservationRequest cancelReservationRequest) {

        GetReservationResponse reservationUpdated =
                reservationService.cancelReservation( id, cancelReservationRequest);

        return new ResponseEntity<>(
                reservationUpdated,
                HttpStatus.OK
        );
    }


    //UPDATE para clientes
    @PutMapping("/update-client/{id}")
    public ResponseEntity<UpdateReservationResponse> updateReservationClient(@PathVariable Integer id,
                @Valid @RequestBody UpdateReservationClientRequest updateReservationClientRequest) throws Exception {

        UpdateReservationResponse reservationUpdated =
                reservationService.updateReservationClient(id, updateReservationClientRequest);

        return new ResponseEntity<>(
                reservationUpdated,
                HttpStatus.OK
        );
    }

    //UPDATE para admin
    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateReservationResponse> updateReservationAdmin(@PathVariable Integer id,
            @Valid @RequestBody UpdateReservationAdminRequest updateReservationAdminRequest) throws Exception {

        UpdateReservationResponse response =
                reservationService.updateReservationAdmin(id, updateReservationAdminRequest);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);

        return new ResponseEntity<>(
                "Reserva eliminada correctamente",
                HttpStatus.OK
        );
    }

}
