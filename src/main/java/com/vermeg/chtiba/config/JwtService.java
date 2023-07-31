package com.vermeg.chtiba.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import com.vermeg.chtiba.repositories.ClientRepository;


@Service
public class JwtService {
    private static final String SECRET_KEY = "3778214125442A472D4B614E645267556B58703273357638792F423F4528482B";

    private static final long EXPIRATION_TIME = 1440000L;

    private static ClientRepository ur;

    public String extractUsername(String token) {
        return ((Claims)Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody()).getSubject();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private static Claims extractClaims(String token) {
        return (Claims)Jwts.parser().setSigningKey("3778214125442A472D4B614E645267556B58703273357638792F423F4528482B".getBytes()).parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetails userDetails) {
        if (userDetails == null)
            throw new IllegalArgumentException("UserDetails cannot be null");
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        if (userDetails == null)
            throw new IllegalArgumentException("UserDetails cannot be null");
        if (extraClaims == null)
            extraClaims = new HashMap<>();
        return
                Jwts.builder()
                        .setClaims(extraClaims)
                        .setSubject(userDetails.getUsername())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 1440000L))
                        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                        .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Hex.decode("3778214125442A472D4B614E645267556B58703273357638792F423F4528482B");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }
}
