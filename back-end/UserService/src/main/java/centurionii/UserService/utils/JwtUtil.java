package centurionii.UserService.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtUtil {

    private static final String SECRET_KEY = System.getenv("JWT_SECRET_KEY");

    public static String generateToken(Long userId) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .setSubject(userId.toString())
                .signWith(key)
                .compact();
    }

    public static Claims validateToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static long getUserIdFromJwt(String authHeader) {
        String token = authHeader.substring(7);
        Claims claims = JwtUtil.validateToken(token);

        String userId = claims.getSubject();

        return Long.parseLong(userId);
    }
}
