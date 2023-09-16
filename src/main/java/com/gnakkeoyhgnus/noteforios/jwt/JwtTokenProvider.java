package com.gnakkeoyhgnus.noteforios.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.repository.UserRepository;
import com.gnakkeoyhgnus.noteforios.exception.CustomException;
import com.gnakkeoyhgnus.noteforios.exception.ErrorCode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  @Value("${spring.jwt.secret-key}")
  private String secretKey;

  @Value("${spring.jwt.expiration}")
  private Long accessTokenExpireTime;

  private final UserRepository userRepository;

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

    log.info("[Token Valid] Token 검증 시작 : " + token);

    try {
      token = token.substring(PREFIX.length());

      log.info("[Token Valid] Token 검증 완료 : " + token);
      return true;
    } catch (SignatureVerificationException e) {
      log.error("[Signature verification failed] : " + e.getMessage());
    } catch (TokenExpiredException e) {
      log.error("[Token expired] : " + e.getMessage());
    } catch (Exception e) {
      log.error("[Invalid token] : " + e.getMessage());
    }

    log.error("[Token Valid] Token 검증 실패 : " + token);
    return false;
  }

  public Authentication getAuthentication(String token) {

    String email = getEmail(token);

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

    return new UsernamePasswordAuthenticationToken(user, "", authorities);
  }

  public String getEmail(String token) {
    token = token.substring(PREFIX.length());
    DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);

    return decodedJWT.getClaim(EMAIL_CLAIM).asString();
  }

  public static Optional<String> resolveToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));
  }

}
