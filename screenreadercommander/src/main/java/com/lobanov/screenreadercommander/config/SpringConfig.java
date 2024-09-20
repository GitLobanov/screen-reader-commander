package com.lobanov.screenreadercommander.config;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.lobanov.screenreadercommander")
public class SpringConfig {

    @Bean
    Tesseract tesseract () throws IOException {
        String datapath = properties().getProperty("datapath");
        String language = properties().getProperty("language");

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(datapath);
        tesseract.setLanguage(language);

        return tesseract;
    }

    @Bean
    Properties properties() throws IOException {
        Properties properties = new Properties();
        FileInputStream input = new FileInputStream("config.txt");
        properties.load(input);
        return properties;
    }


}
