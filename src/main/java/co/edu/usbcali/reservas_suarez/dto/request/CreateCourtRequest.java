package co.edu.usbcali.reservas_suarez.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCourtRequest {
    @NotBlank(message = "El nombre de la cancha es requerido")
    @Size(max = 50, message = "El nombre solo soporta hasta 50 caracteres")
    private String name;
    private String description;
}
