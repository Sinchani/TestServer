package net.media.spamserver.util;

import net.media.spamserver.config.Config;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

public class FileUtil {

    public static String readFile(String fileLocation) {
        int ch;
        StringBuilder strContent = new StringBuilder("");
        InputStream fin;
        try {
            fin = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileLocation);
            while ((ch = fin.read()) != -1) {
                strContent.append((char) ch);
            }
            fin.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return strContent.toString();
    }

    public static InputStream getBotListInputStream() {
        try {
            return new FileInputStream(Config.BOT_LIST_LOCATION);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static FileReader getNonBlockingColoRangeFileReader(String fileNameSuffix) {
        try {
            fileNameSuffix = fileNameSuffix.contains(".txt") ? fileNameSuffix : fileNameSuffix + ".txt";
            return new FileReader(Config.COLO_LIST_LOCATION + Config.COLO_NON_BLOCKING_FILE_PREFIX + fileNameSuffix);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static FileReader getBlockingColoRangeFileReader(String fileNameSuffix) {
        try {
            fileNameSuffix = fileNameSuffix.contains(".txt") ? fileNameSuffix : fileNameSuffix + ".txt";
            return new FileReader(Config.COLO_LIST_LOCATION + Config.COLO_BLOCKING_FILE_PREFIX + fileNameSuffix);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
