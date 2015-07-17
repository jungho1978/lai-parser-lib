package com.lge.lai.parser.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lge.lai.parser.source.ActionNameVisitor;

public class FileWalker {
    static Logger LOGGER = LogManager.getLogger(FileWalker.class.getName());
    
    private String path;
    private int fileCount = 0;
    private int dirCount = 0;

    private String[] extensions;
    private List<String> filePaths;

    public FileWalker(String path, String[] extenstions) {
        this.path = path;
        this.extensions = extenstions;

        this.filePaths = new ArrayList<String>();

        // start walking
        resolve(this.path);
    }

    private void resolve(String path) {
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) {
            return;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                dirCount++;
                resolve(f.getAbsolutePath());
            } else {
                if (matchesWithExtension(f)) {
                    fileCount++;
                    filePaths.add(f.getAbsolutePath());
                }
            }
        }
    }

    private boolean matchesWithExtension(File file) {
        for (String ext : extensions) {
            if (file.getName().endsWith(ext)) {
                LOGGER.info("[" + ext + "] " + file.getAbsolutePath());
                return true;
            }
        }
        return false;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getDirCount() {
        return dirCount;
    }

    public String getPath() {
        return path;
    }

    public String[] getFilePathList() {
        return filePaths.toArray(new String[filePaths.size()]);
    }
}
