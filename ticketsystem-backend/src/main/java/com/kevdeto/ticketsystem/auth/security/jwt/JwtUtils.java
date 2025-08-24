package com.kevdeto.ticketsystem.auth.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Getter
@Component
public class JwtUtils {
	@Value("${jwt.secret.key}")
	private String secretKey;
	@Value("${jwt.secret.key.refresh}")
	private String refreshSecretKey;
	@Value("${jwt.expiration.time}")
	private long accessTokenExpirationMillis;
	@Value("${jwt.refresh.expiration}")
	private long refreshTokenExpirationMillis; // 30 dias
	private Key signingKey;
	private Key signingKeyRefresh;

	public String generateAccessToken(String email) {
		return generateToken(email, "access", accessTokenExpirationMillis);
	}
	
    public String generateRefreshToken(String email, String jti) {
        return Jwts.builder()
                .setSubject(email)
                .claim("token_type", "refresh")
                .claim("jti", jti)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMillis))
                .signWith(signingKeyRefresh, SignatureAlgorithm.HS512)
                .compact();
    }

	public String generateToken(String email, String tokenType, long customExpirationMillis) {
		return Jwts.builder()
				.setSubject(email)
				.claim("token_type", tokenType)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + customExpirationMillis))
				.signWith(signingKey, SignatureAlgorithm.HS512)
				.compact();
	}

	public String renewTokenIfExpiringSoon(String token) {
		Claims claims = extractAllClaims(token);
		Date expiration = claims.getExpiration();
		Date now = new Date();

		// renueva al expirar (15 minutos)
        long tokenDuration = expiration.getTime() - claims.getIssuedAt().getTime();
        if (tokenDuration <= accessTokenExpirationMillis) {
            if (expiration.before(new Date(now.getTime() + 15L * 60 * 1000))) {
                return generateAccessToken(claims.getSubject());
            }
        }
		return token;
	}

	public boolean isTokenValid(String token, String expectedTokenType) {
		try {
			Claims claims;
//			= extractAllClaims(token);
			
	        // Determinar qué clave usar basado en el tipo de token esperado
	        if ("refresh".equals(expectedTokenType)) {
	            claims = extractAllClaimsRefresh(token);  // Usar clave de refresh
	        } else {
	            claims = extractAllClaims(token);         // Usar clave de access
	        }

	        if (expectedTokenType != null && !expectedTokenType.equals(claims.get("token_type", String.class))) {
	            return false;
	        }
			
			return !claims.getExpiration().before(new Date());
		} catch (JwtException e) {
			System.out.println("Invalid JWT: " + e.getMessage());
			return false;
		}
	}
	
    public String extractEmail(String token, boolean isRefreshToken) {
        return extractClaim(token, Claims::getSubject, isRefreshToken);
    }
    
    public Date extractExpiration(String token, boolean isRefreshToken) {
        return extractClaim(token, Claims::getExpiration, isRefreshToken);
    }
    
    public String extractTokenType(String token, boolean isRefreshToken) {
        return extractClaim(token, claims -> claims.get("token_type", String.class), isRefreshToken);
    }
    
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isRefreshToken) {
        Claims claims = isRefreshToken ? extractAllClaimsRefresh(token) : extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public Claims extractAllClaimsRefresh(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKeyRefresh)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public String extractJti(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKeyRefresh)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("jti", String.class);
    }
    
    @PostConstruct
    private void signingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecretKey);
        this.signingKeyRefresh = Keys.hmacShaKeyFor(refreshKeyBytes);
    }
}
