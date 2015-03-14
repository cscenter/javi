package org.antlr.java;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.body.BodyDeclaration;

import java.io.File;
import java.io.IOException;

public class Launcher
{
    public static void recursivePrint(GraphicNode node) {
       if (node.children.size() == 0) {
           node.paint();
           return;
       }

       node.paint();
       for (GraphicNode child : node.children) {
           recursivePrint(child);
       }
    }

    public static void main(String[] args) throws IOException {
        String dirPath = "/home/ees/IdeaProjects/antlr_test/";
        String [] sampleNames = {"NestTest.java"};
        for (String sampleName : sampleNames) {
            CompilationUnit unit = JavaParser.parse(new File(dirPath + sampleName));
            //!!!
            BodyDeclaration method = unit.getTypes().get(0).getMembers().get(0);
            KeyWordsPrinterVisitor visitor = new KeyWordsPrinterVisitor(method);

            System.out.println(sampleName);
            method.accept(visitor, null);

            // Visitor построил нам модель.
            GraphicNode root = visitor.getRoot();
            recursivePrint(root);

//            System.out.println("--------------------");
        }

//        CommentsPrinterVisitor cVisitor = new CommentsPrinterVisitor();
//        CompilationUnit unit = JavaParser.parse(new File(dirPath + "Comments.java"));
//        System.out.println("Comments.java");
//        unit.accept(cVisitor, null);
    }
}
