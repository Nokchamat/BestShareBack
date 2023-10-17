package com.gnakkeoyhgnus.noteforios.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gnakkeoyhgnus.noteforios.domain.entity.PageShareBoard;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final ObjectMetadata objectMetadata = new ObjectMetadata();

  private final static String PREFIX = "https://ios-note-bucket.s3.ap-northeast-2.amazonaws.com/";

  private final AmazonS3Client amazonS3Client;

  public String uploadForProfile(MultipartFile profile, String userEmail) {

    try {
      log.info("[uploadImage 시작]" + " userEmail : " + userEmail);

      objectMetadata.setContentType(profile.getContentType());
      objectMetadata.setContentLength(profile.getSize());

      String fileKey = "profile/" + userEmail;

      amazonS3Client.putObject(bucket, fileKey, profile.getInputStream(), objectMetadata);

      log.info("[uploadImage 완료]" + " userEmail : " + userEmail);
      return amazonS3Client.getUrl(bucket, fileKey).toString();

    } catch (IOException e) {
      e.printStackTrace();
      log.info(e.getMessage());
    }

    return "";
  }

  public String uploadForThumbnail(MultipartFile thumbnail, Long pageShareBoardId) {

    try {
      log.info("[uploadForThumbnail 시작]" + " pageShareBoardId : " + pageShareBoardId);

      objectMetadata.setContentType(thumbnail.getContentType());
      objectMetadata.setContentLength(thumbnail.getSize());

      String fileKey = pageShareBoardId + "/thumbnail";

      amazonS3Client.putObject(bucket, fileKey, thumbnail.getInputStream(), objectMetadata);

      log.info("[uploadForThumbnail 완료]" + " pageShareBoardId : " + pageShareBoardId);
      return amazonS3Client.getUrl(bucket, fileKey).toString();

    } catch (IOException e) {
      e.printStackTrace();
      log.info(e.getMessage());
    }

    return "";
  }

  public String uploadForPDF(MultipartFile pdf, Long pageShareBoardId) {

    try {
      log.info("[uploadPDF 시작]" + " pageShareBoardId : " + pageShareBoardId);

      objectMetadata.setContentType(pdf.getContentType());
      objectMetadata.setContentLength(pdf.getSize());

      String fileKey = pageShareBoardId + "/pdf";

      amazonS3Client.putObject(bucket, fileKey, pdf.getInputStream(), objectMetadata);

      log.info("[uploadPDF 완료]" + " pageShareBoardId : " + pageShareBoardId);
      return amazonS3Client.getUrl(bucket, fileKey).toString();

    } catch (IOException e) {
      e.printStackTrace();
      log.info(e.getMessage());
    }

    return "";
  }

  public void deleteAllFileForPageShareBoard(PageShareBoard pageShareBoard) {
    deleteUploadFile(pageShareBoard.getThumbnailUrl().substring(PREFIX.length()));
    deleteUploadFile(pageShareBoard.getPagePDFUrl().substring(PREFIX.length()));
  }

  public void deleteUploadFile(String key) {
    log.info("[deleteUploadFile 시작]" + " userEmail : " + key);

    amazonS3Client.deleteObject(bucket, key);

    log.info("[deleteUploadFile 완료]" + " key : " + key);
  }

  public String uploadExplainImage(User user, MultipartFile image) {
    try {
      log.info("[uploadExplainImage 시작]" + " userEmail : " + user.getEmail());

      objectMetadata.setContentType(image.getContentType());
      objectMetadata.setContentLength(image.getSize());

      String fileKey = "explain/" + UUID.randomUUID();

      amazonS3Client.putObject(bucket, fileKey, image.getInputStream(), objectMetadata);

      log.info("[uploadExplainImage 완료]" + " userEmail : " + user.getEmail());
      return amazonS3Client.getUrl(bucket, fileKey).toString();

    } catch (IOException e) {
      e.printStackTrace();
      log.info(e.getMessage());
    }

    return "";
  }
}
