package org.antlr.java;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;

import java.io.File;
import java.io.IOException;

public class Launcher
{
    public static void main(String[] args) throws IOException {
        KeyWordsPrinterVisitor visitor = new KeyWordsPrinterVisitor();
        String dirPath = "/Users/dudinviktor/Documents/java/src/";
        String [] sampleNames = { "FactComputer.java", "Factorial.java", "FizzBuzz.java", "FizzBuzz2.java", "KeyValueForeach.java" };
        for (String sampleName : sampleNames) {
            CompilationUnit unit = JavaParser.parse(new File(dirPath + sampleName));
            System.out.println(sampleName);
            unit.accept(visitor, null);
            System.out.println("--------------------");
        }

        CommentsPrinterVisitor cVisitor = new CommentsPrinterVisitor();
        CompilationUnit unit = JavaParser.parse(new File(dirPath + "Comments.java"));
        System.out.println("Comments.java");
        unit.accept(cVisitor, null);
    }
}
