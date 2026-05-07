package co.edu.usbcali.reservas_suarez.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UpdateClientRequest {
    private String name;
    private String phone;
}
