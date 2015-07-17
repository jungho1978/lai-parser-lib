package com.lge.lai.parser;

import com.google.common.annotations.VisibleForTesting;
import com.lge.lai.parser.manifest.ManifestParser;
import com.lge.lai.parser.source.SourceParser;
import com.lge.lai.parser.util.FileWalker;

public class Launcher {
    @VisibleForTesting
    public static boolean SKIP_CHECK = false;
    
	private static final String XML = "xml";
	private static final String JAR = "jar";
	private static final String JAVA = "java";
    private static final String ANDROID_MANIFEST = "AndroidManifest.xml";
    
    public void run(String path, ManifestParser parser) {
        FileWalker xml = new FileWalker(path, new String[] { XML });
        for (String file : xml.getFilePathList()) {
            if (file.endsWith(ANDROID_MANIFEST) && !ignoresManifest(file)) {
                parser.parse(file);
            }
        }
    }
    
    public void run(String path, SourceParser parser) {
        FileWalker jars = new FileWalker(path, new String[] { JAR });
        FileWalker javas = new FileWalker(path, new String[] { JAVA });
        for (String file : javas.getFilePathList()) {
            parser.parse(new String[] { path }, jars.getFilePathList(), file);
        }
    }

    private boolean ignoresManifest(String filepath) {
        if (SKIP_CHECK) {
            return false;
        }
        
        String[] candidates = new String[] { "test", "overlay" };
        for (String candidate : candidates) {
            if (filepath.toLowerCase().contains(candidate)) {
                return true;
            }
        }
        return false;
    }
    
    @VisibleForTesting
    public static void setSkipCheck(boolean isSkip) {
        SKIP_CHECK = isSkip;
    }
}
