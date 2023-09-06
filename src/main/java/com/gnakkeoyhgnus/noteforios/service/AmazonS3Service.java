package com.gnakkeoyhgnus.noteforios.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {

  @Value("${BUCKET_NAME")
  private String bucket;

  private final AmazonS3Client amazonS3Client;

  public String uploadImage(MultipartFile image, String userEmail) {

    try {
      log.info("[uploadImage 시작]" + " userEmail : " + userEmail);

      ObjectMetadata metadata = new ObjectMetadata();

      metadata.setContentType(image.getContentType());
      metadata.setContentLength(image.getSize());

      String fileKey = "image/" + userEmail + UUID.randomUUID();

      amazonS3Client.putObject(bucket, fileKey, image.getInputStream(), metadata);

      log.info("[uploadImage 완료]" + " userEmail : " + userEmail);
      return amazonS3Client.getUrl(bucket, fileKey).toString();

    } catch (IOException e) {
      e.printStackTrace();
      log.info(e.getMessage());
    }

    return "";
  }

  public String uploadPDF(MultipartFile file, String userEmail) {

    try {
      log.info("[uploadPDF 시작]" + " userEmail : " + userEmail);

      ObjectMetadata metadata = new ObjectMetadata();

      metadata.setContentType(file.getContentType());
      metadata.setContentLength(file.getSize());

      String fileKey = "pdf/" + userEmail + UUID.randomUUID();

      amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);

      log.info("[uploadPDF 완료]" + " userEmail : " + userEmail);
      return amazonS3Client.getUrl(bucket, fileKey).toString();

    } catch (IOException e) {
      e.printStackTrace();
      log.info(e.getMessage());
    }

    return "";
  }

  public String getFile(String fileKey) {
    log.info("[getFile 시작]" + " fileKey : " + fileKey);

    return amazonS3Client.getUrl(bucket, fileKey).toString();
  }

}
