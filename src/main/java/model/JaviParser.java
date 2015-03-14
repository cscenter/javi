package model;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.body.BodyDeclaration;
import com.github.antlrjavaparser.api.body.MethodDeclaration;
import com.github.antlrjavaparser.api.body.TypeDeclaration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Anastasiia Kuzenkova on 10.03.15.
 */
public class JaviParser
{
    private HashMap<String, MethodDeclaration> mMethodHashMap;

    public JaviParser(File file)
    {
        mMethodHashMap = new HashMap<>();
        try {
            CompilationUnit unit = JavaParser.parse(file);
            for (TypeDeclaration typeDeclaration : unit.getTypes()) {
                for (BodyDeclaration bodyDeclaration : typeDeclaration.getMembers()) {
                    if (bodyDeclaration instanceof MethodDeclaration) {
                        MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                        mMethodHashMap.put(methodDeclaration.getName(), methodDeclaration);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] methodNames()
    {
        return mMethodHashMap.keySet().toArray(new String[mMethodHashMap.keySet().size()]);
    }

    public String getMethodBodyByName(String methodName)
    {
        return mMethodHashMap.get(methodName).toString();
    }
}
