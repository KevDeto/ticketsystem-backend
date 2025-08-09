package com.kevdeto.ticketsystem.auth.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtils {
	@Value("${jwt.secret.key}")
	private String secretKey;
	@Value("${jwt.expiration.time}")
	private long accessTokenExpirationMillis;
	private final long refreshTokenExpirationMillis = 30L * 24 * 60 * 60 * 1000; // 30 dias
	private Key signingKey;

//	public JwtUtils(String secretKey, long accessTokenExpirationMillis, Key signingKey) {
//		this.secretKey = secretKey;
//		this.accessTokenExpirationMillis = accessTokenExpirationMillis;
//		this.signingKey = getSigningKey();
//	}

	public String generateAccessToken(String username) {
		return generateToken(username, accessTokenExpirationMillis);
	}
	
    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenExpirationMillis);
    }

	public String generateToken(String username, long customExpirationMillis) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + customExpirationMillis))
				.signWith(signingKey, SignatureAlgorithm.HS256).compact();
	}

	public String renewTokenIfExpiringSoon(String token) {
		Claims claims = extractAllClaims(token);
		Date expiration = claims.getExpiration();
		Date now = new Date();

		// renueva al expirar (7 dias)
		if (expiration.before(new Date(now.getTime() + 7L * 24 * 60 * 60 * 1000))) {
			return generateAccessToken(claims.getSubject());
		}
		return token;
	}

	public boolean isTokenValid(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(signingKey)
			.build()
			.parseClaimsJws(token)
			.getBody();

			return true;
		} catch (JwtException e) {
			System.out.println("Invalid JWT: " + e.getMessage());
			return false;
		}
	}
	
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    @PostConstruct
    private void signingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }
}
