package com.lge.lai.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ParserOptions {
	CommandLine command;
	
	public ParserOptions(String[] args) {
		Options options = new Options();
		options.addOption("p", "path", true, "project directory path");
		options.addOption("include_android_jar", "include_android_jar", false, "includes android.jar as extra library");
		try {
			command = new DefaultParser().parse(options, args);
			if (!command.hasOption("p")) {
			    showHelp(options);
			    throw new RuntimeException();
			}
		} catch (ParseException e) {
			showHelp(options);
			throw new RuntimeException();
		}
			
	}
	
	public String getOption(String opt) {
		return command.getOptionValue(opt);
	}
	
	public boolean hasOption(String opt) {
		return command.hasOption(opt);
	}
	
	private void showHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(100);
		formatter.printHelp("java -jar lai-parser-lib.jar", options);
	}
}
