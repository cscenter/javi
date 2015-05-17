package model;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.body.BodyDeclaration;
import com.github.antlrjavaparser.api.body.MethodDeclaration;
import com.github.antlrjavaparser.api.body.TypeDeclaration;
import com.github.antlrjavaparser.api.stmt.BlockStmt;
import com.github.antlrjavaparser.api.stmt.Statement;
import com.github.antlrjavaparser.api.stmt.TypeDeclarationStmt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockSchemeFileBuilder {
    private Map<String, ArrayList<BlockScheme>> bs = new HashMap<>();

    public Map<String, ArrayList<BlockScheme>> getBlockScheme() {
        return bs;
    }

    public BlockSchemeFileBuilder(String path) {
        File file = new File(path);

        try {
            CompilationUnit fileParsed = JavaParser.parse(file);
            List<TypeDeclaration> classes = fileParsed.getTypes();
            List<TypeDeclaration> allClasses = new ArrayList<>();

            for (TypeDeclaration cl : classes) {
                allClasses.add(cl);
                innerClasses(cl, allClasses);
            }

            for (TypeDeclaration cls : allClasses) {
                String className = cls.getName();
                bs.put(className, new ArrayList<>());
                List<BodyDeclaration> members = cls.getMembers();
                List<MethodDeclaration> methods = new ArrayList<>();

                for (BodyDeclaration member : members) {
                    if (member instanceof MethodDeclaration) {
                        methods.add((MethodDeclaration) member);
                    }
                }

                for (MethodDeclaration method : methods) {
                   BlockScheme blockScheme = new BlockScheme();
                   String nameMethod = method.getName();
                   List<com.github.antlrjavaparser.api.body.Parameter> parameters = method.getParameters();
                   if (parameters != null) {
                        for (com.github.antlrjavaparser.api.body.Parameter parameter : parameters) {
                            nameMethod = nameMethod + "_" + parameter.getType().toString();
                        }
                   }
                    blockScheme.setNameMethod(nameMethod);
                    BlockSchemeBuilder visitor = new BlockSchemeBuilder(blockScheme);
                    method.accept(visitor, null);
                    visitor.finish();
                    bs.get(className).add(blockScheme);
                }
            }
        //EL: throw to View and show there    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void innerClasses(TypeDeclaration cl, List<TypeDeclaration> allClasses) {
        List<BodyDeclaration> members = cl.getMembers();

        for (BodyDeclaration body : members) {
            if (body instanceof TypeDeclaration) {
                allClasses.add((TypeDeclaration) body);
                innerClasses((TypeDeclaration) body, allClasses);
            } else if (body instanceof MethodDeclaration) {
                BlockStmt bodystmt = ((MethodDeclaration) body).getBody();
                List<Statement> inMethod = bodystmt.getStmts();
                if (inMethod != null) {
                    for (Statement st : inMethod) {
                        if (st instanceof TypeDeclarationStmt) {
                            allClasses.add(((TypeDeclarationStmt) st).getTypeDeclaration());
                            innerClasses(((TypeDeclarationStmt) st).getTypeDeclaration(), allClasses);
                        }
                    }
                }
            }
        }
    }
}