package com.gnakkeoyhgnus.noteforios.config;

import static com.gnakkeoyhgnus.noteforios.jwt.JwtTokenProvider.PREFIX;

import com.gnakkeoyhgnus.noteforios.exception.CustomException;
import com.gnakkeoyhgnus.noteforios.exception.ErrorCode;
import com.gnakkeoyhgnus.noteforios.jwt.JwtTokenProvider;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    log.info("preSend 시작");
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {

      String token = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
      if (token != null && token.startsWith(PREFIX)) {
        log.info("preSend PREFIX 확인");
        if (jwtTokenProvider.isTokenValid(token)) {
          log.info("preSend TokenValid 완료");
          Authentication auth = jwtTokenProvider.getAuthentication(token);
          Objects.requireNonNull(accessor.getSessionAttributes())
              .put("user", auth.getPrincipal());

          log.info("preSend Authentication 작업 완료");
        }
      } else {
        log.error("토큰이 없거나 형식이 맞지 않습니다.");
        throw new CustomException(ErrorCode.PERMISSION_DENIED);
      }

    }

    return message;
  }
}
