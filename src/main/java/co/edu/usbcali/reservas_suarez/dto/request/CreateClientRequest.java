package co.edu.usbcali.reservas_suarez.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateClientRequest {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre solo soporta hasta 100 caracteres")
    private String name;

    @NotBlank(message = "El teléfono es requerido")
    @Size(max = 20, message = "El teléfono solo soporta hasta 20 caracteres")
    private String phone;
}

