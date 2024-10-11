package org.bohdanzhuvak.nicoai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableCaching
public class NicoAiApplication {

  public static void main(String[] args) {
    SpringApplication.run(NicoAiApplication.class, args);
  }

}
