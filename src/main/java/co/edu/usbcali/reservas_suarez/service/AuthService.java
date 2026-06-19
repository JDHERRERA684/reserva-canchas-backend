package co.edu.usbcali.reservas_suarez.service;

import co.edu.usbcali.reservas_suarez.dto.request.LoginRequest;
import co.edu.usbcali.reservas_suarez.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(
            LoginRequest loginRequest
    );
}