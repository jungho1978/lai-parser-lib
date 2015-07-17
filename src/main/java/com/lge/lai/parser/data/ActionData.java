package com.lge.lai.parser.data;

import com.google.common.base.Objects;

public class ActionData {
    public String packageName;
    public String className;
    public String declarationType;
    public String lineNumber;
    public String actionName;
    public boolean isResolved;
    
    public ActionData() {
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("packageName", packageName)
                .add("className", className)
                .add("declarationType", declarationType)
                .add("lineNumber", lineNumber)
                .add("actionName", actionName)
                .add("isResolved", isResolved)
                .toString();
    }
}
