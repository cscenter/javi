package model;
import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.body.BodyDeclaration;
import com.github.antlrjavaparser.api.body.MethodDeclaration;
import com.github.antlrjavaparser.api.body.TypeDeclaration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModelTest {
    public static void main(String[] args) throws IOException {
	String examplePath = args[0];
        File file = new File(examplePath);
        // parse file
        CompilationUnit fileParsed = JavaParser.parse(file);

        List<TypeDeclaration> classes = fileParsed.getTypes();
        List<BodyDeclaration> members = classes.get(0).getMembers();

        List<MethodDeclaration> methods = new ArrayList<>();
        for (int i = 0; i < members.size(); ++i)
        {
            if (members.get(i) instanceof MethodDeclaration)
            {
                methods.add((MethodDeclaration) members.get(i));
            }
        }

        // find main
        MethodDeclaration main_ = null;
        for (int i = 0; i < methods.size(); ++i)
        {
            if (methods.get(i).getName().equals("main")) {
                main_ = methods.get(i);
                break;
            }
        }
        assert main_ != null;

        // accept visitor in main.
        BlockScheme blockScheme = new BlockScheme();
        BlockSchemeBuilder visitor = new BlockSchemeBuilder(blockScheme);
        main_.accept(visitor, null);
        visitor.finish();

        //System.out.println(blockScheme);
    }
}