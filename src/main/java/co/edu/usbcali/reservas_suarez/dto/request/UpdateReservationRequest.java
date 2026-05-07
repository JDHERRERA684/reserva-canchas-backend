package co.edu.usbcali.reservas_suarez.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor

public class UpdateReservationRequest {
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String notes;
}
