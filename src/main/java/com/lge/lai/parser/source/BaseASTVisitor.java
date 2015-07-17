package com.lge.lai.parser.source;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.lge.lai.parser.ParserListener;

public class BaseASTVisitor extends ASTVisitor {
    public CompilationUnit cu;
    public ParserListener listener;

    public BaseASTVisitor(CompilationUnit cu, ParserListener listener) {
        this.cu = cu;
        this.listener = listener;
    }

    public boolean visit(MethodInvocation node) {
        return false;
    }
    
    public String getPackageName() {
        return cu.getPackage().getName().toString();
    }
    
    @SuppressWarnings("rawtypes")
    public String getClassName() {
        List types = cu.types();
        TypeDeclaration typeDec = (TypeDeclaration)types.get(0);
        return typeDec.getName().toString();
    }
    
    public boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
