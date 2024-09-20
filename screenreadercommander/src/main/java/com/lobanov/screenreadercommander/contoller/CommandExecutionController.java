package com.lobanov.screenreadercommander.contoller;

import com.lobanov.screenreadercommander.util.ValidateUtils;
import com.lobanov.screenreadercommander.service.ScreenReaderService;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

@Component
public class CommandExecutionController {

    private ScreenReaderService screenReaderService;
    private Properties properties;

    public CommandExecutionController(ScreenReaderService screenReaderService, Properties properties) {
        this.screenReaderService = screenReaderService;
        this.properties = properties;
    }

    public void executeImagineCommand() {

        String triggerText = properties.getProperty("trigger.text");
        String fileCommandsName = properties.getProperty("file.commands.name");

        List<String> commands = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileCommandsName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                commands.add(ValidateUtils.validateCommand(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (commands.isEmpty()) {
            System.out.println("There is no commands in files");
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Void> task = () -> {
            screenReaderService.searchTextAndDoCommand(triggerText, commands);
            return null;
        };

        Future<Void> future = executor.submit(task);

        try {
            future.get();
            System.out.println("Command execution completed");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }

}
