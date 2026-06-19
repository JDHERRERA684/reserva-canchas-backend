package co.edu.usbcali.reservas_suarez.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CancelReservationRequest {

    @NotBlank(message = "El reservationCode no puede estar vacío")
    private String reservationCode;
}