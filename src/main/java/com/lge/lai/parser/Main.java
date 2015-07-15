package com.lge.lai.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.common.annotations.VisibleForTesting;
import com.lge.lai.common.db.dao.DAOProperties;
import com.lge.lai.parser.manifest.ManifestParser;
import com.lge.lai.parser.report.Report;
import com.lge.lai.parser.report.ReportFactory;

public class Main {
    public static Launcher launcher = new Launcher();

    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("p", "path", true, "project directory path");

        CommandLine command = new DefaultParser().parse(options, args);
        if (!command.hasOption("p")) {
            showHelp(options);
            System.exit(-1);
        }

        String path = command.getOptionValue("p");
        String[] tokens = path.split(getSeperatorForRegex());
        String name = tokens[tokens.length - 1];

        ParserProperties properties = new ParserProperties("LGAppIF.parser");
        String reportType = properties.getProperty("report", true);
        boolean writeDB = Boolean.valueOf(properties.getProperty("write.db", true));

        Report report = ReportFactory.create(reportType, name);
        ManifestParser manifestParser = new ManifestParser(report, writeDB);

        launcher.run(path, manifestParser);
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

    private static void showHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp("java -jar LGAppInterfaceParser.jar", options);
    }
}
