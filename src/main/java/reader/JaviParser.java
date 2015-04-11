package reader;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.body.BodyDeclaration;
import com.github.antlrjavaparser.api.body.MethodDeclaration;
import com.github.antlrjavaparser.api.body.TypeDeclaration;
import model.JaviNode;
import view.JaviBlockSchemeBlock;
import view.JaviBlockSchemeBlockType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class JaviParser
{
    private HashMap<String, JaviBlockSchemeBlock> mMethodHashMap;

    public JaviParser(File file)
    {
        mMethodHashMap = new HashMap<>();
        try {
            CompilationUnit unit = JavaParser.parse(file);
            for (TypeDeclaration typeDeclaration : unit.getTypes()) {
                for (BodyDeclaration bodyDeclaration : typeDeclaration.getMembers()) {
                    if (bodyDeclaration instanceof MethodDeclaration) {
                        MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                        JaviBlockSchemeVisitor visitor = new JaviBlockSchemeVisitor();
                        JaviBlockSchemeBlock root = new JaviBlockSchemeBlock(JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeMethod, methodDeclaration.getName(), 1);
                        methodDeclaration.getBody().accept(visitor, root.getNestedStatements().get(0));
                        mMethodHashMap.put(methodDeclaration.getName(), root);
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

    public JaviBlockSchemeBlock getMethodBodyByName(String methodName)
    {
        return mMethodHashMap.get(methodName);
    }

    private String methodString(JaviNode node) {
        if (node.children.size() == 0) {
            return node.paint();
        }

        StringBuilder result = new StringBuilder(node.paint());
        for (JaviNode child : node.children) {
            result.append(methodString(child));
        }
        return result.toString();
    }
}
