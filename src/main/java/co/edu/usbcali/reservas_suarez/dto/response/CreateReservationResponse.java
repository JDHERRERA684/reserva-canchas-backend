package co.edu.usbcali.reservas_suarez.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateReservationResponse {
    private Integer id;

    private String clientName;
    private String courtName;
    private String statusName;

    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

    private String notes;
    private String reservationCode;
}
