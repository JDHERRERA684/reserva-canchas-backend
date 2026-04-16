package co.edu.usbcali.reservas_suarez.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateClientRequest {

    private String name;
    private String phone;
}

