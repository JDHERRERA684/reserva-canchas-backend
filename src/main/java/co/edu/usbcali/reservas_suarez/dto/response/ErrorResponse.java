package co.edu.usbcali.reservas_suarez.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder

public class ErrorResponse {
    private String message;
    private Integer status;
    private LocalDateTime timestamp;
}
