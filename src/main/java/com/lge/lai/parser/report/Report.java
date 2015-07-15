package com.lge.lai.parser.report;

public interface Report {
    void addCategory(String category);

    void setData(String category, Object obj) throws Exception;
}
