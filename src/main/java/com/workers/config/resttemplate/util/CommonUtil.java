package com.workers.config.resttemplate.util;

import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
public class CommonUtil {

    public static PublicKey getPublicKeyFromPem(String publicKeyPEM) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(publicKeyPEM);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            log.error("[getPublicKeyFromPem] Invalid public key");
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid public key");
        }
    }
}
