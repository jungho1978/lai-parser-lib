package com.lge.lai.parser.data;

import com.google.common.base.Objects;

public class AndroidAction {
    public String packageName;
    public String className;
    public String declartionType;
    public String lineNumber;
    public String actionName;
    public boolean isResolved = true;
    
    public AndroidAction() {
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("packageName", packageName)
                .add("className", className)
                .add("declarationType", declartionType)
                .add("lineNumber", lineNumber)
                .add("actionName", actionName)
                .add("isResolved", isResolved)
                .toString();
    }
}
