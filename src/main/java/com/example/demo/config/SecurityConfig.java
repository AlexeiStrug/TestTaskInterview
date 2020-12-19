package com.example.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SecurityConfig {

    public String passwordEncoder(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public boolean matchPasswords(String passwordFromUser, String passwordFromDb) {
        return BCrypt.checkpw(passwordFromUser, passwordFromDb);
    }

    public String createJWT(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 50000))
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
    }

    public String parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey("secret")
                .parseClaimsJws(jwt)
                .getBody();

        return claims.getSubject();
    }
}
