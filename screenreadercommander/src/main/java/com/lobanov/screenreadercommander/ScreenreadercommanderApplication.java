package com.lobanov.screenreadercommander;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;

@SpringBootApplication
public class ScreenreadercommanderApplication {

    private static final Map<String, String> textMap = new HashMap<>();
    private static final String triggerText = "Generation completed";



    public static void main(String[] args) {

        textMap.put("key1", "Text to text");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Void> task = () -> {
            try {

                Properties properties = new Properties();
                FileInputStream input = new FileInputStream("config.txt");
                properties.load(input);

                String datapath = properties.getProperty("datapath");
                String language = properties.getProperty("language");

                Robot robot = new Robot();
                Tesseract tesseract = new Tesseract();
                tesseract.setDatapath(datapath);
                tesseract.setLanguage(language);

                for (Map.Entry<String, String> entry : textMap.entrySet()) {
                    boolean triggerFound = false;

                    while (!triggerFound) {
                        BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

                        String recognizedText = tesseract.doOCR(screenCapture);

                        if (recognizedText.contains(triggerText)) {
                            System.out.println("Триггер найден: " + triggerText);

                            String textToPrint = entry.getValue();
                            printText(robot, textToPrint);

                            triggerFound = true;
                        } else {
                            System.out.println("Триггер не найден, ждем 3 минуты...");
                            // Засыпаем на 3 минуты перед следующей проверкой
                            TimeUnit.SECONDS.sleep(5);
                        }
                    }
                }
            } catch (TesseractException | AWTException | InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        };

//            StringSelection stringSelection = new StringSelection(recognizedText);
//            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);


//            robot.keyPress(KeyEvent.VK_CONTROL);
//            robot.keyPress(KeyEvent.VK_V); // Вставить текст из буфера обмена
//            robot.keyRelease(KeyEvent.VK_V);
//            robot.keyRelease(KeyEvent.VK_CONTROL);

//            robot.keyPress(KeyEvent.VK_ENTER);
//            robot.keyRelease(KeyEvent.VK_ENTER);


        Future<Void> future = executor.submit(task);

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();

//        SpringApplication.run(ScreenreadercommanderApplication.class, args);
    }

    private static void printText(Robot robot, String text) {
        for (char c : text.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED != keyCode) {
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
            }
        }

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        System.out.println("Напечатано: " + text);
    }

//    Generation completed

}
