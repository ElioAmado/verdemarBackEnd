package com.verdemar.service.Config;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;

public class EjemploConfig {
    public static void main(String[] args) {
        String filePath = "config.properties"; // nombre del archivo
        File configFile = new File(filePath);

        // Si no existe, lo crea vacío
        if (!configFile.exists()) {
            try {
                if (configFile.createNewFile()) {
                    System.out.println("Archivo de configuración creado: " + filePath);
                }
            } catch (IOException e) {
                System.err.println("No se pudo crear el archivo de configuración.");
                e.printStackTrace();
                return;
            }
        }

        // Configuramos el builder
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<>(
                PropertiesConfiguration.class)
                .configure(params.properties()
                        .setFileName(filePath));

        try {
            PropertiesConfiguration config = builder.getConfiguration();

            // Si no existe una propiedad, la añade
            if (!config.containsKey("usuario")) {
                config.setProperty("usuario", "admin");
                builder.save();
                System.out.println("Propiedad 'usuario' creada con valor 'admin'.");
            }

            // Leer una propiedad existente
            String user = config.getString("usuario");
            System.out.println("Usuario: " + user);

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
