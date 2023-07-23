package com.example.springboot.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "pGwjipnZw/QdEHhOgct+VlubKpdX8VCbxRGynNj8kJeD/WyWBZxA3Yp6ZoIhKCkLuwpDM5Yx/73NdCqk7Q63vPxtE6g7KNaItRoWv6ZFERWvS1hrBj7CC3DJFoS/2HC6ILHJYBr6O5yD+lco/uZ5Bkvd8pdWyxmSlCXYlclNe+GAF1unuq8UcM09eczHrqiFNK7Gt4kbcQQHMtyx7ejDECvPlLMI6RD/uVarpMY4L4QGhmnu7+RXU8Tn8Bb252LDWyHH2bp6E6uQvpqvNwYqa3jyfWiTEZ2C1VDkOaKrjVEsC2baq/rmsc/E+fMc26QxeqYS2xn+w7Qd4/FO37/VzdLkd8oPTxZwe3/pn2mX3tY=";
    private static final int EXPIRATION_DURATION = 1000 * 60 * 24;

    //create  a method that validates if a token is valid: Username extracted from the token is the same as the passed username in userDetails
    //the token did not expire
    public boolean isTokenValid(String jwt, UserDetails userDetails){

        String username = extractUsername(jwt);
        return Objects.equals(username, userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    //create a method that checks if the token expired or not
    public boolean isTokenExpired(String jwt){
        return extractExpirationDate(jwt).before(new Date(System.currentTimeMillis()));
    }
    // this method overloading is used to create a token with only user details and no extra information.
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>() , userDetails);
    }

    public  String generateToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails){

            return Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DURATION))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
    }


    //here we pass the claim resolver getSubject to the subject claim containing the username
    public String extractUsername(String jwt){
        return extractClaim(jwt, Claims::getSubject);
    }


    //create a method that extract the expiration Date out of the jwt
    public Date extractExpirationDate(String jwt){
        return extractClaim(jwt,Claims::getExpiration);
    }


    //this method is used as a strategy patter to get from the all claims extracted from the jwt the claim that we want specificaly
    //according to the claims resolver that we pass
    public <T> T extractClaim(String jwt, Function<Claims,T> claimsResolver ){
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    //this method is used to parse the jwt using our singing key and returning all the claims in the jwt
    private Claims extractAllClaims (String jwt){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
