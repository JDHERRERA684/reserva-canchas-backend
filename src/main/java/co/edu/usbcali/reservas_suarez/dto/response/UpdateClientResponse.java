package co.edu.usbcali.reservas_suarez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor

public class UpdateClientResponse {
    private Integer id;
    private String name;
    private String phone;
}
