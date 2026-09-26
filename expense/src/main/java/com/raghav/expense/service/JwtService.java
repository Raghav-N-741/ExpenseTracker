package com.raghav.expense.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.raghav.expense.model.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service 
public class JwtService {
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    public SecretKey getKey()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(String email)
    {
        return Jwts.builder()
        .subject(email)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000*60*60*8))
        .signWith(getKey())
        .compact();
    }

    public Claims extractAllClaims(String token)
    {
        return Jwts.parser()
               .verifyWith(getKey())
               .build()
               .parseSignedClaims(token)
               .getPayload();
    }

    public <T> T extractClaims(String token,Function<Claims,T> claimResolver)
    {
        Claims claims=extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String extractEmail(String token)
    {
        return extractClaims(token,Claims::getSubject);
    }

    public Date extractExpiration(String token)
    {
        return extractClaims(token,Claims::getExpiration);
    }

    public boolean isTokenValid(String token,UserDetails userdetails)
    {
        UserPrincipal u=(UserPrincipal)userdetails;
        if(!u.getEmail().equals(extractEmail(token))) return false;
        return new Date(System.currentTimeMillis()).before(extractExpiration(token));
    }
}
