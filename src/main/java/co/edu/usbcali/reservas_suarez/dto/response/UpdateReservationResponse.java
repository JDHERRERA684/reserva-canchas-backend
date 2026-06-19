package co.edu.usbcali.reservas_suarez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor

public class UpdateReservationResponse {

    private Integer id;

    private String clientName;
    private String courtName;
    private String statusName;

    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

    private String notes;
    private String reservationCode;
}
