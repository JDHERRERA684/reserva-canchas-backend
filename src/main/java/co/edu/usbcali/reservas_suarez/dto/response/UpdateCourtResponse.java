package co.edu.usbcali.reservas_suarez.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class UpdateCourtResponse {
    private Integer id;
    private String name;
    private String description;
}
