package com.lge.lai.parser.source;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import com.google.common.collect.Lists;
import com.lge.lai.parser.ParserListener;
import com.lge.lai.parser.data.IntentMethodData;


public class IntentMethodVisitor extends BaseASTVisitor {
    static Logger LOGGER = LogManager.getLogger(IntentMethodVisitor.class.getName());
    public static final String CATEGORY = "IntentMethod";
    private static final List<String> methodChecks = Lists.newArrayList();
    static {
        // Activity
        methodChecks.add("public void startActivities(android.content.Intent[], android.os.Bundle)");
        methodChecks.add("public void startActivities(android.content.Intent[])");
        methodChecks.add("public void startActivity(android.content.Intent, android.os.Bundle)");
        methodChecks.add("public void startActivity(android.content.Intent)");
        methodChecks.add("public void startActivityForResult(android.content.Intent, int)");
        methodChecks.add("public void startActivityForResult(android.content.Intent, int, android.os.Bundle)");
        methodChecks.add("public void startActivityFromChild(android.app.Activity, android.content.Intent, int, android.os.Bundle)");
        methodChecks.add("public void startActivityFromChild(android.app.Activity, android.content.Intent, int)");
        methodChecks.add("public void startActivityFromFragment(android.app.Fragment, android.content.Intent, int, android.os.Bundle)");
        methodChecks.add("public void startActivityFromFragment(android.app.Fragment, android.content.Intent, int)");
        methodChecks.add("public boolean startActivityIfNeeded(android.content.Intent, int, android.os.Bundle)");
        methodChecks.add("public boolean startActivityIfNeeded(android.content.Intent, int)");
        methodChecks.add("public boolean startNextMatchingActivity(android.content.Intent)");
        methodChecks.add("public boolean startNextMatchingActivity(android.content.Intent, android.os.Bundle)");
        // Receiver
        methodChecks.add("public void sendBroadcast(android.content.Intent)");
        methodChecks.add("public void sendBroadcast(android.content.Intent, java.lang.String)");
        methodChecks.add("public void sendBroadcastAsUser(android.content.Intent, android.os.UserHandle)");
        methodChecks.add("public void sendBroadcastAsUser(android.content.Intent, android.os.UserHandle, java.lang.String)");
        methodChecks.add("public void sendOrderedBroadcast(android.content.Intent, java.lang.String, android.content.BroadcastReceiver, android.os.Handler, int, java.lang.String, android.os.Bundle)");
        methodChecks.add("public void sendOrderedBroadcast(android.content.Intent, java.lang.String)");
        methodChecks.add("public void sendOrderedBroadcastAsUser(android.content.Intent, android.os.UserHandle, java.lang.String, android.content.BroadcastReceiver, android.os.Handler, int, java.lang.String, android.os.Bundle)");
        methodChecks.add("public void sendStickyBroadcast(android.content.Intent)");
        methodChecks.add("public void sendStickyBroadcastAsUser(android.content.Intent, android.os.UserHandle)");
        methodChecks.add("public void sendStickyOrderedBroadcast(android.content.Intent, android.content.BroadcastReceiver, android.os.Handler, int, java.lang.String, android.os.Bundle)");
        methodChecks.add("public void sendStickyOrderedBroadcastAsUser(android.content.Intent, android.os.UserHandle, android.content.BroadcastReceiver, android.os.Handler, int, java.lang.String, android.os.Bundle)");
        // Service
        methodChecks.add("public boolean bindService(android.content.Intent, android.content.ServiceConnection, int)");
        methodChecks.add("public void unbindService(android.content.ServiceConnection)");
        methodChecks.add("public android.content.ComponentName startService(android.content.Intent)");
        methodChecks.add("public boolean stopService(android.content.Intent)");
    }
    
    public IntentMethodVisitor(CompilationUnit cu, ParserListener listener) {
        super(cu, listener);
        listener.onStarted(CATEGORY);
    }
    
    public boolean visit(MethodInvocation node) {
        IMethodBinding binding = node.resolveMethodBinding();
        if (binding != null) {
            if (methodChecks.contains(binding.toString().trim())) {
                IntentMethodData method = new IntentMethodData();
                method.packageName = getPackageName();
                method.className = getClassName();
                method.lineNumber = String.valueOf(cu.getLineNumber(node.getStartPosition() - 1));
                method.methodName = node.toString();
                if (!isEmpty(method.methodName)) {
                    method.isResolved = true;
                }
                LOGGER.info(method);
                listener.onFound(CATEGORY, method);
            }
        }
        return false;
    }

}
