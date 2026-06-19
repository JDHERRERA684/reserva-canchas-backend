package co.edu.usbcali.reservas_suarez.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.List;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        //  PRUEBA System.out.println("JWT FILTER EJECUTANDO");

        // Obtener Authorization Header
        String authHeader =
                request.getHeader("Authorization");

        // Validar si existe Bearer Token
        if (
                authHeader == null
                        || !authHeader.startsWith("Bearer ")
        ) {

            //PRUEBA System.out.println("NO HAY TOKEN");

            filterChain.doFilter(request, response);
            return;
        }

        // Extraer token
        String token =
                authHeader.substring(7);

        //PRUEBA System.out.println("TOKEN RECIBIDO: " + token);

        // Extraer username
        String username =
                jwtService.extractUsername(token);

        //PRUEBA System.out.println("USERNAME JWT: " + username);

        // Extraer role
        String role =
                jwtService.extractRole(token);

        //PRUEBA System.out.println("ROLE JWT: " + role);

        // Crear autenticación
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        token,
                        List.of(
                                new SimpleGrantedAuthority(role)
                        )
                );

        // Registrar autenticación en Spring Security
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        System.out.println("USUARIO AUTENTICADO");

        filterChain.doFilter(request, response);
    }
}