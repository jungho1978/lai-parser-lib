package com.lge.lai.parser.report;

public class ReportFactory {
    public static Report create(String type, String name) {
        if (type.equals("excel")) {
            return new ExcelReport(name);
        }
        return new DummyReport(name);
    }
}
