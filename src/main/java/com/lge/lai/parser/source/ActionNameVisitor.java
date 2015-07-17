package com.lge.lai.parser.source;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

import com.lge.lai.parser.ParserListener;
import com.lge.lai.parser.data.ActionData;

public class ActionNameVisitor extends BaseASTVisitor {
    static Logger LOGGER = LogManager.getLogger(ActionNameVisitor.class.getName());

    public static final String CATEGORY = "Action";

    private static final String INTENT_QUALIFIED_NAME = "android.content.Intent";
    private static final String SEC_ACTION_METHOD = "android.content.Intent setAction(java.lang.String)";

    public ActionNameVisitor(CompilationUnit cu, ParserListener listener) {
        super(cu, listener);
        listener.onStarted(CATEGORY);
    }

    public boolean visit(ClassInstanceCreation node) {
        ITypeBinding typeBinding = node.resolveTypeBinding();
        if (typeBinding != null) {
            if (typeBinding.getQualifiedName().equals(INTENT_QUALIFIED_NAME)) {
                IMethodBinding constructorBinding = node.resolveConstructorBinding();
                ITypeBinding[] params = constructorBinding.getParameterTypes();
                for (int i = 0; i < params.length; i++) {
                    if (params[i].getName().equalsIgnoreCase("string")) {
                        parseObject("CIC", node.arguments().get(i));
                    }
                }
            }
        }
        return false;
    }
    
    @SuppressWarnings({ "unchecked" })
    public boolean visit(MethodInvocation node) {
        IMethodBinding binding = node.resolveMethodBinding();
        if (binding != null) {
            String method = binding.toString().trim();
            if (method.contains(SEC_ACTION_METHOD)) {
                List<Object> args = node.arguments();
                if (args.size() == 1) {
                    parseObject("MI", args.get(0));
                }
            }
        }
        return false;
    }

    private void parseObject(String declarationType, Object obj) {
        ActionData action = new ActionData();
        action.packageName = getPackageName();
        action.className = getClassName();
        action.declarationType = declarationType;

        if (obj instanceof StringLiteral) {
            StringLiteral literal = (StringLiteral)obj;
            action.lineNumber = String.valueOf(cu.getLineNumber(literal.getStartPosition() - 1));
            action.actionName = literal.toString().replaceAll("\"", "");
            action.isResolved = true;
        } else if (obj instanceof QualifiedName) {
            QualifiedName qualified = (QualifiedName)obj;
            action.lineNumber = String.valueOf(cu.getLineNumber(qualified.getStartPosition() - 1));
            action.actionName = (String)qualified.resolveConstantExpressionValue();
            if (!isEmpty(action.actionName)) {
                action.isResolved = true;
            }
        } else if (obj instanceof SimpleName) {
            SimpleName simple = (SimpleName)obj;
            action.lineNumber = String.valueOf(cu.getLineNumber(simple.getStartPosition() - 1));
            action.actionName = (String)simple.resolveConstantExpressionValue();
            if (!isEmpty(action.actionName)) {
                action.isResolved = true;
            }
        } else {
            LOGGER.error("Unknown instance from " + action.className + " in " + action.packageName);
        }
        LOGGER.info(action.toString());
        listener.onFound(CATEGORY, action);
    }
}
