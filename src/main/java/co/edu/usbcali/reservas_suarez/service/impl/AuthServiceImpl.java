package co.edu.usbcali.reservas_suarez.service.impl;
import co.edu.usbcali.reservas_suarez.dto.request.LoginRequest;
import co.edu.usbcali.reservas_suarez.dto.response.LoginResponse;
import co.edu.usbcali.reservas_suarez.exception.ResourceNotFoundException;
import co.edu.usbcali.reservas_suarez.model.User;
import co.edu.usbcali.reservas_suarez.repository.UserRepository;
import co.edu.usbcali.reservas_suarez.security.JwtService;
import co.edu.usbcali.reservas_suarez.service.AuthService;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class AuthServiceImpl implements AuthService  {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public LoginResponse login(
            LoginRequest loginRequest
    ) {

        // Buscar usuario
        User user = userRepository.findByUsername(
                        loginRequest.getUsername()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Validar contraseña
        boolean passwordCorrect =
                passwordEncoder.matches(
                        loginRequest.getPassword(),
                        user.getPasswordHash()
                );

        if (!passwordCorrect) {
            throw new RuntimeException(
                    "Contraseña incorrecta"
            );
        }

        // Generar token JWT
        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRole()
        );

        // Retornar response
        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

}
