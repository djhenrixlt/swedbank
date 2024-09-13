package com.example.swedbankApi.user.config;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final String jwtSecret = generateSecretKey();


    public String generateToken(Authentication authentication) {
        // Get the username from the authentication object
        String username = authentication.getName();

        // Get the current date and set the expiration date (1 hour later)
        Date currentDate = new Date();
        long jwtExpirationDate = 3600000; // 1 hour
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        // Extract the roles (authorities) from the authentication object
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Get the string representation of each role
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(username) // Set the subject as the username
                .claim("roles", roles) // Add roles to the JWT claims
                .setIssuedAt(currentDate) // Set issued date
                .setExpiration(expireDate) // Set expiration date
                .signWith(key(), SignatureAlgorithm.HS256) // Sign the token
                .compact();// Return the generated token
    }


    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // extract username from JWT token
    public String getUsername(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // validate JWT token
    public boolean validateToken(String token) {
        Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parse(token);
        return true;

    }

    public String generateSecretKey() {
        // length means (32 bytes are required for 256-bit key)
        int length = 32;

        // Create a secure random generator
        SecureRandom secureRandom = new SecureRandom();

        // Create a byte array to hold the random bytes
        byte[] keyBytes = new byte[length];

        // Generate the random bytes
        secureRandom.nextBytes(keyBytes);

        // Encode the key in Base64 format for easier storage and usage
        return Base64.getEncoder().encodeToString(keyBytes);
    }
}
