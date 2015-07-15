package com.lge.lai.parser.data;

import com.google.common.base.Objects;

public class AndroidPassingIntentMethod {
    public String packageName;
    public String className;
    public String lineNumber;
    public String methodName;
    public boolean isResolved = true;
    
    public AndroidPassingIntentMethod() {
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("packageName", packageName)
                .add("className", className)
                .add("lineNumber", lineNumber)
                .add("methodName", methodName)
                .add("isResvoled", isResolved)
                .toString();
    }
}