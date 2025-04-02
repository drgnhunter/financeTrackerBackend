package com.example.finance;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.security.Key;

public class JwtTokenUtil {

    // Secret key for signing the JWT (make sure to store this securely)
    private static final String SECRET_KEY = "d@#36W5aK^9vJ@1L!mPt@29Y2Z$QxW8f";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour expiration time

    // Method to generate JWT token
    public static String generateToken(String username) {
        // Create signing key from the secret key
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        // Create and return the JWT token
        return Jwts.builder()
                .setSubject(username)  // Set the username as the subject
                .setIssuedAt(new Date())  // Set the issued time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Set expiration time
                .signWith(key, SignatureAlgorithm.HS256)  // Sign the token with HMAC using the secret key
                .compact();  // Build and compact the JWT token
    }

    // Method to validate the token and extract claims
    public static Claims extractClaims(String token) {
        // Create signing key from the secret key
         // Create signing key from the secret key
    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Parse the token and extract claims using the older Jwts.parser()
    return Jwts.parser()
               .setSigningKey(key)  // Set the signing key to verify the token signature
               .parseClaimsJws(token)  // Parse the JWT and extract the claims
               .getBody();  // Get the claims from the JWT body
    }

    // Method to extract the username from the token
    public static String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Method to check if the token has expired
    public static boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Method to validate if the token is still valid
    public static boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    public static String validateTokenAndGetUsername(String token) {
        // Validate if the token is valid
        if (isTokenExpired(token)) {
            throw new RuntimeException("Token has expired");
        }
    
        // Extract the username from the token if it's valid
        return extractUsername(token);
    }
    
}
