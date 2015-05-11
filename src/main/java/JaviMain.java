import startDialog.JaviFrame;
import model.*;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.body.*;
import view.JaviBlockSchemePictureTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JaviMain
{
    private static void createAndShowGUI()
    {
        JaviFrame frame = new JaviFrame();
        frame.pack();
        frame.setVisible(true);
    }

    private static void testNewModel()	throws Exception {
		File file = new File("examples/FizzBuzz.java");
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

	//	System.out.println(blockScheme);     		

    }

    public static void main(String[] args) throws Exception {
    	System.out.println("params:" + args.length);
        if (args.length == 1 && args[0].startsWith("-")) {
            String arg = args[0];
            if (arg.equals("-png")) {
                JaviBlockSchemePictureTest.saveAll();
            }
        }
        else if (args.length == 2) {
            JaviBlockSchemePictureTest.saveToPng(args);
        }
     	else {	
        	javax.swing.SwingUtilities.invokeLater(JaviMain::createAndShowGUI);
        }
    }
}
