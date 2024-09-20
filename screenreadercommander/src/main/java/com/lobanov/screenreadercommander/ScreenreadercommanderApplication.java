package com.lobanov.screenreadercommander;

import com.lobanov.screenreadercommander.config.SpringConfig;
import com.lobanov.screenreadercommander.contoller.CommandExecutionController;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class ScreenreadercommanderApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        CommandExecutionController commandExecutionController = ctx.getBean(CommandExecutionController.class);

        commandExecutionController.executeImagineCommand();
    }

}
