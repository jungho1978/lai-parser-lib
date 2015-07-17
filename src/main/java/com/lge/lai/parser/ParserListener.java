package com.lge.lai.parser;

public interface ParserListener {
    public void onStarted(String category);

    public void onFound(String category, Object object);
}
