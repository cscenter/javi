package reader;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.body.BodyDeclaration;
import com.github.antlrjavaparser.api.body.MethodDeclaration;
import com.github.antlrjavaparser.api.body.TypeDeclaration;
import model.BlockScheme;
import model.BlockSchemeBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class JaviParser
{
    private HashMap<String, BlockScheme> mMethodHashMap;

    public JaviParser(File file)
    {
        mMethodHashMap = new HashMap<>();
        try {
            CompilationUnit unit = JavaParser.parse(file);
            for (TypeDeclaration typeDeclaration : unit.getTypes()) {
                for (BodyDeclaration bodyDeclaration : typeDeclaration.getMembers()) {
                    if (bodyDeclaration instanceof MethodDeclaration) {
                        MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                        BlockScheme blockScheme = new BlockScheme();
                        BlockSchemeBuilder visitor = new BlockSchemeBuilder(blockScheme);
                        methodDeclaration.accept(visitor, null);
                        visitor.finish();

                        mMethodHashMap.put(methodDeclaration.getName(), blockScheme);
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

    public BlockScheme getMethodBodyByName(String methodName)
    {
        return mMethodHashMap.get(methodName);
    }
}