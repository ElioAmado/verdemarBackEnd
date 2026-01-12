package com.verdemar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class VerdemarApplication {

  @Autowired
  private static Logger logger = LoggerFactory.getLogger(VerdemarApplication.class);
  public static void main(String[] args) {
    logger.info("Arrancando la aplicación Verdemar");
    logger.debug("Modo debug activado");
    SpringApplication.run(VerdemarApplication.class, args);
  }
}
