package com.lge.lai.parser.source;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.lge.lai.parser.ParserListener;
import com.lge.lai.parser.report.Report;
import com.lge.lai.parser.util.FileToText;

public class SourceParser implements ParserListener {
    static Logger LOGGER = LogManager.getLogger(SourceParser.class.getName());
    private Report report;
    private boolean writeDB;

    public SourceParser(Report report, boolean writeDB) {
        this.report = report;
        this.writeDB = writeDB;
    }
    
    public void parse(String[] sourceEntries, String[] libraryEntries, String filePath) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setStatementsRecovery(true);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setCompilerOptions(JavaCore.getOptions());
        parser.setUnitName("");
        
        parser.setEnvironment(libraryEntries, sourceEntries, new String[] { "UTF-8" }, true);
        
        String source = "";
        try {
            source = new FileToText(new File(filePath)).get();
        } catch (IOException e) {
            LOGGER.error("Exception while converting to text: " + e);
            e.printStackTrace();
        }
        parser.setSource(source.toCharArray());
        
        final CompilationUnit cu = (CompilationUnit)parser.createAST(null);
        
        cu.accept(new ActionNameVisitor(cu, this));
        cu.accept(new IntentMethodVisitor(cu, this));
    }

    @Override
    public void onStarted(String category) {
        report.addCategory(category);
    }

    @Override
    public void onFound(String category, Object object) {
        try {
            report.setData(category, object);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

}
