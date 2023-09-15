package com.gnakkeoyhgnus.noteforios.config.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class JwtTokenProvider {

  @Value("${spring.jwt.secret-key}")
  private String secretKey;

  @Value("${spring.jwt.access.expiration}")
  private Long accessTokenExpireTime;

  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String EMAIL_CLAIM = "email";
  private static final String PREFIX = "Bearer ";

  public String createAccessToken(String email) {
    Date now = new Date();
    return PREFIX + JWT.create()
        .withIssuer("note")
        .withSubject(ACCESS_TOKEN_SUBJECT)
        .withAudience("user")
        .withExpiresAt(new Date(now.getTime() + accessTokenExpireTime))
        .withIssuedAt(new Date(now.getTime()))
        .withClaim(EMAIL_CLAIM, email)
        .sign(Algorithm.HMAC256(secretKey));
  }

  public boolean isTokenValid(String token) {
    try {
      token = token.substring(PREFIX.length());
      JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);

      return true;
    } catch (SignatureVerificationException e) {
      log.error("Signature verification failed: {}", e.getMessage());
    } catch (TokenExpiredException e) {
      log.error("Token expired: {}", e.getMessage());
    } catch (Exception e) {
      log.error("Invalid token: {}", e.getMessage());
    }

    return false;
  }

  public String getEmail(String token) {
    token = token.substring(PREFIX.length());
    DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);

    return decodedJWT.getClaim("email").toString();
  }

}
