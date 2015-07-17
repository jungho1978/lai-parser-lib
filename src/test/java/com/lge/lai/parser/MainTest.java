package com.lge.lai.parser;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class MainTest {
    @Test
    //@Ignore
    public void run() throws ParseException {
        Launcher.SKIP_CHECK = true;
        String[] args = new String[] { "-p", TestEnv.PATH };
        Main.main(args);
    }
}
