package co.edu.usbcali.reservas_suarez.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Clave secreta para firmar el token JWT.
    private final String SECRET_KEY =
            "mysecretkeymysecretkeymysecretkeymysecretkey123456";

    // Genera un token JWT con username y role.
    public String generateToken(
            String username,
            String role
    ) {
        Key key = Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes()
        );

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60 * 24
                        )
                )
                .signWith(
                        key,
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    // Extrae username del token.
    public String extractUsername(
            String token
    ) {

        return extractAllClaims(token)
                .getSubject();
    }

    // Extrae role del token.
    public String extractRole(
            String token
    ) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    // Extrae todos los claims del JWT.
    private Claims extractAllClaims(
            String token
    ) {

        return Jwts.parserBuilder()

                .setSigningKey(
                        SECRET_KEY.getBytes()
                )

                .build()

                .parseClaimsJws(token)

                .getBody();
    }
}
