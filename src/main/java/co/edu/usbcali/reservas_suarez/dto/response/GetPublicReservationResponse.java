package co.edu.usbcali.reservas_suarez.dto.response;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetPublicReservationResponse {
    private Integer id;
    private Integer courtId;
    private String courtName;
    private Integer statusId;
    private String statusName;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String reservationCode;


}
