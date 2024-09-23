package com.lobanov.screenreadercommander.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ScreenReaderService {

    private CommandService commandService;
    private Tesseract tesseract;

    public ScreenReaderService(CommandService commandService, Tesseract tesseract) {
        this.commandService = commandService;
        this.tesseract = tesseract;
    }

    public void searchTextAndDoCommand(String triggerText, List<String> commands) {
        for (String cmd : commands) {
            boolean triggerFound = false;

            while (!triggerFound) {

                try {
                    Robot robot = new Robot();
                    BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

                    String recognizedText = tesseract.doOCR(screenCapture);

                    if (recognizedText.contains(triggerText)) {
                        System.out.println("Trigger found");
                        commandService.doTextCommand(cmd);
                        triggerFound = true;
                        TimeUnit.SECONDS.sleep(5);
                    } else {
                        if (recognizedText.contains("Generation in progress")) {
                            System.out.println("Generation in progress, wait one minute...");
                        } else {
                            System.out.println("Trigger not found, wait one minute...");
                        }
                        TimeUnit.MINUTES.sleep(1);
                    }
                } catch (TesseractException | AWTException | InterruptedException e) {
                    e.printStackTrace();
                    break;
                }

            }
        }
    }

}
