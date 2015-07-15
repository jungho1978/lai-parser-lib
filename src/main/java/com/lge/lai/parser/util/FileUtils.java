package com.lge.lai.parser.util;

import java.io.File;

public class FileUtils {
    public static boolean exists(String filepath) {
        return new File(filepath).exists();
    }
}
