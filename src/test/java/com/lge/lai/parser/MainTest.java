package com.lge.lai.parser;

import java.io.File;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MainTest {
    private static String PACKAGE_PATH;

    @Before
    public void setUp() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String filepath = classLoader.getResource("AndroidManifest.xml").getFile();
        PACKAGE_PATH = new File(filepath).getParent();
    }

    @Test
    @Ignore
    public void run() throws ParseException {
        String[] args = new String[] { "-p", PACKAGE_PATH };
        Main.main(args);
    }
}
