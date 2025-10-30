package com.verdemar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class VerdemarApplication {

  @Autowired
  private static Logger logger = LogManager.getLogger(VerdemarApplication.class);
  public static void main(String[] args) {
    logger.info("Arrancando la frutería 🍎");
    logger.debug("Modo debug activado");
    logger.error("¡Algo ha fallado con las naranjas!");
    SpringApplication.run(VerdemarApplication.class, args);
  }
}
