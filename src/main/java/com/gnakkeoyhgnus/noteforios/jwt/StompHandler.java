package com.gnakkeoyhgnus.noteforios.jwt;

import com.gnakkeoyhgnus.noteforios.exception.CustomException;
import com.gnakkeoyhgnus.noteforios.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {

    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    log.info("[preSand] websocket 검증 시작 토큰 : " + accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION));

    if (accessor.getCommand() == StompCommand.CONNECT) {
      if (!jwtTokenProvider.isTokenValid(
          accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION))) {
        throw new CustomException(ErrorCode.PERMISSION_DENIED);
      }
    }
    log.info("[preSand] websocket 검증 완료");
    return message;
  }
}
