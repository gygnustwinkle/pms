package com.yql.common.utils;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

/**
 * JWT验证过滤器：配置顺序 CorsFilte->JwtUtilsr-->StrutsPrepareAndExecuteFilter
 *
 */
/**
 * 解密jwt，获得所有声明(包括标准和私有声明)
 *
 * @param
 * @return
 * @throws Exception
 */
@Component
public class JwtUtil {
    private JwtUtil(){}
    //有效时间
    private static final long JWT_EXPIRE = 60*30*1000;//半小时
    //private static final long JWT_EXPIRE = 10*1000;//半小时
    //令牌密钥
    private static final String JWT_KEY = "!D7#YT^%#FH^S@&DF#K$G*JK&%U%K5@T";

    public static String createToken(Object data){
        //当前时间
        long currentTime = System.currentTimeMillis();
        //过期时间
        long expTime = currentTime + JWT_EXPIRE;
        //构建Jwt
        JwtBuilder system = Jwts.builder()
                .setId(UUID.randomUUID() + "")
                .setSubject(JSON.toJSONString(data))
                .setIssuer("system")
                .setIssuedAt(new Date(currentTime))
                .signWith(encodeSecret(JWT_KEY),SignatureAlgorithm.HS256)
                .setExpiration(new Date(expTime));
        return system.compact();
    }

    private static SecretKey encodeSecret(String key) {
        byte[] encode = Base64.getEncoder().encode(key.getBytes());
        SecretKeySpec aes = new SecretKeySpec(encode,0,encode.length,"HmacSHA256");
        return aes;
    }

    public static Claims parseToken(String token){
        Claims body = Jwts.parser()
                .setSigningKey(encodeSecret(JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        return body;
    }

    public static <T> T parseToken(String token,Class<T> clazz){
        Claims body = Jwts.parser()
                .setSigningKey(encodeSecret(JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        return JSON.parseObject(body.getSubject(),clazz);
    }


}