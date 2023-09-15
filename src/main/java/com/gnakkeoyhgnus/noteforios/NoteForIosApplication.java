package com.gnakkeoyhgnus.noteforios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NoteForIosApplication {

  public static void main(String[] args) {
    SpringApplication.run(NoteForIosApplication.class, args);
  }

}
