package com.gnakkeoyhgnus.noteforios.jwt;

import com.gnakkeoyhgnus.noteforios.exception.CustomException;
import com.gnakkeoyhgnus.noteforios.exception.ErrorCode;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    Optional<String> token = JwtTokenProvider.resolveToken(request);
    if (token.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    String email = jwtTokenProvider.getEmail(token.get());
    log.info("[JwtAuthenticationFilter] TokenVerify 시작 Email : " + email);

    if (!jwtTokenProvider.isTokenValid(token.get())) {
      log.info("[JwtAuthenticationFilter] TokenVerify 실패 Email : " + email);
      throw new CustomException(ErrorCode.INVALID_TOKEN);
    }

    log.info("[JwtAuthenticationFilter] TokenVerify 완료 Email : " + email);
    SecurityContextHolder.getContext().setAuthentication(
        jwtTokenProvider.getAuthentication(token.get()));
    filterChain.doFilter(request, response);
  }

}
