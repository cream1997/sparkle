package com.cream.sparkle.common.utils;

import com.cream.sparkle.common.error.Err;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("complexKey".repeat(4).getBytes());
    // 过期时间
    private static final long EXPIRATION = Times.ONE_HOUR;

    private static final String QuanXianClaimKey = "QX";

    public static String generateToken(long id, String quanXian) {
        return Jwts.builder()
                .subject(Long.toString(id))
                .claim(QuanXianClaimKey, quanXian)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static IdAndQx extractIdAndQx(String token) throws Err {
        try {
            Claims payload = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String id = payload.getSubject();
            String quanXian = payload.get(QuanXianClaimKey, String.class);
            return new IdAndQx(id, quanXian);
        } catch (Exception e) {
            throw new Err("token解析异常", e);
        }
    }

    public static class IdAndQx {
        public final long id;
        public final String quanXian;

        public IdAndQx(String id, String quanXian) {
            this.id = Long.parseLong(id);
            this.quanXian = quanXian;
        }
    }
}
