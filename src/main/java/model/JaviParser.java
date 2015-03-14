package model;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.body.BodyDeclaration;
import com.github.antlrjavaparser.api.body.MethodDeclaration;
import com.github.antlrjavaparser.api.body.TypeDeclaration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Anastasiia Kuzenkova on 10.03.15.
 */
public class JaviParser
{
    public static ArrayList<String> methodsByFile(File file)
    {
        ArrayList<String> result = new ArrayList<>();
        try {
            CompilationUnit unit = JavaParser.parse(file);
            for (TypeDeclaration typeDeclaration : unit.getTypes()) {
                for (BodyDeclaration bodyDeclaration : typeDeclaration.getMembers()) {
                    MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                    if (methodDeclaration != null) {
                        result.add(methodDeclaration.getName());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
