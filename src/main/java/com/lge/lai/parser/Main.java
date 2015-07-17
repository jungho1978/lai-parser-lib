package com.lge.lai.parser;

import org.apache.commons.cli.ParseException;

import com.google.common.annotations.VisibleForTesting;
import com.lge.lai.parser.manifest.ManifestParser;
import com.lge.lai.parser.report.Report;
import com.lge.lai.parser.report.ReportFactory;
import com.lge.lai.parser.source.SourceParser;

public class Main {
	private static final String PROPERTY_KEY = "LGAppIF.parser";
	private static final String PROPERTY_REPORT_OPT = "report";
	private static final String PROPERTY_WRITEDB_OPT = "write.db";
	
	private static final String OPTIONS_PATH = "p";
	private static final String OPTIONS_INCLUDE_ANDROID_JAR = "include_android_jar";
	
    public static Launcher launcher = new Launcher();

    public static void main(String[] args) throws ParseException {
    	ParserProperties properties = new ParserProperties(PROPERTY_KEY);
    	ParserOptions options = new ParserOptions(args);
    	
    	String reportType = properties.getProperty(PROPERTY_REPORT_OPT, true);
    	boolean writeDB = Boolean.valueOf(properties.getProperty(PROPERTY_WRITEDB_OPT, true));
    	String path = options.getOption(OPTIONS_PATH);
    	boolean includeAndroidJar = options.hasOption(OPTIONS_INCLUDE_ANDROID_JAR);
    			
    	String[] tokens = path.split(getSeperatorForRegex());
    	String name = tokens[tokens.length - 1];

        Report report = ReportFactory.create(reportType, name);
        launcher.run(path, new ManifestParser(report, writeDB));
        launcher.run(path, new SourceParser(report, writeDB));
    }

    @VisibleForTesting
    public static void setLauncher(Launcher launcher) {
        Main.launcher = launcher;
    }

    private static String getSeperatorForRegex() {
        if (System.getProperty("os.name").contains("Windows")) {
            return "\\\\";
        } else {
            return "/";
        }
    }
}
