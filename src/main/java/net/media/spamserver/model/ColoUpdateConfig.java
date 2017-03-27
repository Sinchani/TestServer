package net.media.spamserver.model;

import java.io.FileReader;

/**
 * Created by vivek on 4/17/15.
 */
public class ColoUpdateConfig {
    private FileReader fileReader;
    private String listKey;

    public ColoUpdateConfig(FileReader fileReader, String listKey) {
        this.fileReader = fileReader;
        this.listKey = listKey;
    }

    public FileReader getFileReader() {
        return fileReader;
    }

    public void setFileReader(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public String getListKey() {
        return listKey;
    }

    public void setListKey(String listKey) {
        this.listKey = listKey;
    }
}
