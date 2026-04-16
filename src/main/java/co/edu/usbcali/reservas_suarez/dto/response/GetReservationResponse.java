package co.edu.usbcali.reservas_suarez.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder

public class GetReservationResponse {
    private Integer id;

    private Integer clientId;
    private String clientName;

    private Integer courtId;
    private String courtName;

    private Integer statusId;
    private String statusName;

    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

    private String createdBy;
    private String notes;

    private LocalDateTime createdAt;

}
