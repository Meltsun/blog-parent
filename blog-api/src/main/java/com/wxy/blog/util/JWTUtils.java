package com.wxy.blog.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtBuilder;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

public class JWTUtils {

//    private static final String jwtToken = "testsecretkey";
//    static SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtToken));
    static SecretKey key = Jwts.SIG.HS256.key().build();
    public static String createToken(Long userId){
        JwtBuilder jwtBuilder = Jwts.builder()
                .claim("userId",userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 24L * 60 * 60 * 60 * 1000))
                .signWith(key);
        return jwtBuilder.compact();
    }

    public static Optional<Long> checkToken(String token){
        Long userId = null;
        try {
            Jws<Claims> parse = Jwts.parser()
                    .verifyWith(key)
                    .build().parseSignedClaims(token);
            userId = parse.getPayload().get("userId",Long.class);
        }catch (Exception e){
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return Optional.ofNullable(userId);
    }

    public static void main(String[] args) {
        String token = createToken(123L);
        Optional<Long> id = checkToken(token);
        System.out.println(id.map(Object::toString).orElse("未找到用户"));
    }
}

