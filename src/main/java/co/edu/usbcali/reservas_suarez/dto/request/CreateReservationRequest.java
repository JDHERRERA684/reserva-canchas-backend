package co.edu.usbcali.reservas_suarez.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor


public class CreateReservationRequest {
    private Integer clientId;
    private Integer courtId;
    private Integer statusId;

    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

    private String createdBy;
    private String notes;

}
