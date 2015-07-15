package com.lge.lai.parser;

import com.lge.lai.parser.manifest.ManifestParser;
import com.lge.lai.parser.util.FileWalker;

public class Launcher {
    private static final String ANDROID_MANIFEST = "AndroidManifest.xml";

    public void run(String path, ManifestParser parser) {
        FileWalker xml = new FileWalker(path, new String[] { "xml" });
        for (String file : xml.getFilePathList()) {
            if (file.endsWith(ANDROID_MANIFEST) && !ignoresManifest(file)) {
                try {
                    parser.parse(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean ignoresManifest(String filepath) {
        String[] candidates = new String[] { "test", "overlay" };
        for (String candidate : candidates) {
            if (filepath.toLowerCase().contains(candidate)) {
                return true;
            }
        }
        return false;
    }
}
