package com.lobanov.screenreadercommander.util;

public class ValidateUtils {

    public static String validateCommand(String command) {
        command = replaceAll(command,
                "'", "\"", "prompt:", "-");
        return command;
    }

    private static String replaceAll (String line, String... regex) {
        for (String r : regex) {
            line = line.replaceAll(r," ");
        }
        return line;
    }

}
