package com.workers.config.security.util;

import com.workers.config.security.context.TokenAuthenticationFilterContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class SecurityValidationUtil {

    @Value("${jwt.public.key}")
    private String publicKeyString;

    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        publicKey = CommonUtil.getPublicKeyFromPem(publicKeyString);
    }

    public Boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(TokenAuthenticationFilterContext context) {
        return extractClaim(getToken(context), Claims::getSubject);
    }

    public String getToken(TokenAuthenticationFilterContext context) {
        String header = context.getRequest().getHeader(Constants.AUTH_HEADER_NAME);
        return header.replaceAll(Constants.AUTH_TOKEN_PREFIX, "");
    }

    public String getHeaderRequest(TokenAuthenticationFilterContext context) {
        return context.getRequest().getHeader(Constants.AUTH_HEADER_NAME);
    }

    public boolean whenHeaderMissing(TokenAuthenticationFilterContext context) {
        String header = getHeaderRequest(context);
        return Strings.isEmpty(header)
                || !header.startsWith(Constants.AUTH_TOKEN_PREFIX);
    }
    public boolean whenUsernameMissing(TokenAuthenticationFilterContext context) {
        return getUsername(context) == null;
    }

    public boolean whenTokenExpired(TokenAuthenticationFilterContext context) {
        var claims = extractAllClaims(getToken(context));
        return isTokenExpired(claims);
    }

    public List<GrantedAuthority> getGrantedAuthority(TokenAuthenticationFilterContext context) {
        Claims claims = extractAllClaims(getToken(context));
        String roles = (String) claims.get("roles");

        return Arrays.stream(roles.split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(toList());
    }
}
