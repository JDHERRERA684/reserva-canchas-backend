package co.edu.usbcali.reservas_suarez.dto.response;

import lombok.Builder;
import lombok.Getter;




@Builder
@Getter

public class GetClientResponse {
    private Integer id;
    private String name;
    private String phone;


}
