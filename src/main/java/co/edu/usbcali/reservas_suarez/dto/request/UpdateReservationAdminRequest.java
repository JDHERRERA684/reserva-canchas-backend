package co.edu.usbcali.reservas_suarez.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor

public class UpdateReservationAdminRequest {

    @NotNull(message = "El courtId es requerido")
    private Integer courtId;

    @NotNull(message = "El statusId es requerido")
    private Integer statusId;

    @NotNull(message = "La fecha inicial es requerida")
    private LocalDateTime startDatetime;

    @NotNull(message = "La fecha final es requerida")
    private LocalDateTime endDatetime;

    private String notes;
}