package com.gnakkeoyhgnus.noteforios.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailService {

  private final JavaMailSender javaMailSender;

  public void sendMail(String userEmail, String code) {

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
      mimeMessageHelper.setTo(userEmail); // 메일 수신자
      mimeMessageHelper.setSubject("BestShare 인증 코드"); // 메일 제목
      mimeMessageHelper.setText("인증 코드 : " + code); // 메일 본문 내용, HTML 여부

      javaMailSender.send(mimeMessage);

      log.info(userEmail + " : 인증 코드 발송 Success");
    } catch (MessagingException e) {
      log.info("fail");
      throw new RuntimeException(e);
    }
  }

}