package com.gnakkeoyhgnus.noteforios.controller;

import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.form.DeleteImageForm;
import com.gnakkeoyhgnus.noteforios.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/image-upload")
public class ImageUploadController {

  private final AmazonS3Service amazonS3Service;

  @PostMapping
  public ResponseEntity<String> uploadImage(@AuthenticationPrincipal User user,
      MultipartFile image) {

    return ResponseEntity.ok(amazonS3Service.uploadExplainImage(user, image));
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteImage(@RequestBody DeleteImageForm deleteImageForm) {

    amazonS3Service.deleteUploadFile(deleteImageForm.getUploadedFileUrl());

    return ResponseEntity.ok(null);
  }

}
