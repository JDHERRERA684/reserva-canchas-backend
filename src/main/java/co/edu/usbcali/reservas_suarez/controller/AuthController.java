package co.edu.usbcali.reservas_suarez.controller;
import co.edu.usbcali.reservas_suarez.dto.request.LoginRequest;
import co.edu.usbcali.reservas_suarez.dto.response.LoginResponse;
import co.edu.usbcali.reservas_suarez.service.AuthService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor

@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest
    ) {

        LoginResponse response =
                authService.login(loginRequest);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

}
