package model;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.body.BodyDeclaration;
import com.github.antlrjavaparser.api.body.MethodDeclaration;
import com.github.antlrjavaparser.api.body.TypeDeclaration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BlockSchemeFileBuilder {
    Map<String, ArrayList<BlockScheme>> bs = new HashMap<>();

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
                List<BodyDeclaration> members = cl.getMembers();

                for (BodyDeclaration body : members) {
                    if (body instanceof TypeDeclaration) {
                        allClasses.add((TypeDeclaration) body);
                    }
                }
            }

            for (TypeDeclaration cls : allClasses) {
                String className = cls.getName();
                bs.put(className, new ArrayList<>());
                List<BodyDeclaration> members = cls.getMembers();
                List<MethodDeclaration> methods = new ArrayList<>();

                for (int i = 0; i < members.size(); ++i) {
                    if (members.get(i) instanceof MethodDeclaration) {
                        methods.add((MethodDeclaration) members.get(i));
                    }
                }

                for (MethodDeclaration method : methods) {
                    BlockScheme blockScheme = new BlockScheme();
                    BlockSchemeBuilder visitor = new BlockSchemeBuilder(blockScheme);
                    method.accept(visitor, null);
                    visitor.finish();
                    bs.get(className).add(blockScheme);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        BlockSchemeFileBuilder builder = new BlockSchemeFileBuilder("examples/common/Class3.java");
        Map<String, ArrayList<BlockScheme>> bs = builder.getBlockScheme();
        for (String key : bs.keySet()) {
            System.out.println(key + " : " + bs.get(key).size());
        }
//        File file = new File("examples/common/Class2.java");
//        CompilationUnit parse = JavaParser.parse(file);
//        List<TypeDeclaration> types = parse.getTypes();
//        List<BodyDeclaration> members = types.get(0).getMembers();
//        for (BodyDeclaration bd : members) {
//            System.out.println(bd);
//        }
    }
}