package co.edu.usbcali.reservas_suarez.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor

public class UpdateReservationClientRequest {

    @NotNull(message = "El courtId es requerido")
    private Integer courtId;

    @NotNull(message = "La fecha inicial es requerida")
    private LocalDateTime startDatetime;

    @NotNull(message = "La fecha final es requerida")
    private LocalDateTime endDatetime;

    @NotBlank(message = "El reservationCode es requerido")
    private String reservationCode;

    private String notes;
}