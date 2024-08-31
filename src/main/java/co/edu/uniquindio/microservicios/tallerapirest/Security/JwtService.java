package co.edu.uniquindio.microservicios.tallerapirest.Security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "1S2e3C4r5E6t7K8e9Y0c9A8f7E6c5L4i3C2k1S2e3B4a5S6t7I8a9N0a9L8e7J6a5N4d3R2o1S2a3N4t5I6a7G8o9";

    public String getToken(String username) {
        // Construir el token JWT
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("ingesis.uniquindio.edu.co")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora de expiración
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean isTokenValid(String token, String username){
        return getSubject(token).equals(username) && getIssuer(token).equals("ingesis.uniquindio.edu.co")
                && !isTokenExpired(token);
    }

    public String getSubject(String token){
        return getClaim(token, Claims::getSubject);
    }

    public String getIssuer(String token){
        return getClaim(token, Claims::getIssuer);
    }

    public Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = this.getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(this.getKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}