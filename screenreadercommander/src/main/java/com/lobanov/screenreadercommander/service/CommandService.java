package com.lobanov.screenreadercommander.service;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.event.KeyEvent;

@Service
public class CommandService {

    public CommandService() {
    }

    public void doTextCommand(String cmd) throws AWTException {
        Robot robot = new Robot();
        for (char c : cmd.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED != keyCode) {
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
            }
        }

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        System.out.println("Wrote: " + cmd);
    }

}
