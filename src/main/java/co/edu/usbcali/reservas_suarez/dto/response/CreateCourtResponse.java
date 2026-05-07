package co.edu.usbcali.reservas_suarez.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class CreateCourtResponse {
    private Integer id;
    private String name;
    private String description;
}
