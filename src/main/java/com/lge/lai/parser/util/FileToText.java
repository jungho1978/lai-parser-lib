package com.lge.lai.parser.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileToText {
    private File file;
    private StringBuilder data;

    public FileToText(File file) throws IOException {
        this.file = file;
        data = new StringBuilder();

        // start converting
        convert();
    }

    private void convert() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                "UTF-8"));

        String line = null;
        String ls = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            data.append(line);
            data.append(ls);
        }

        reader.close();
    }

    public String get() {
        return data.toString();
    }
}
