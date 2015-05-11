import model.ModelTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class NewModelTest {

    private static File mSimpleExamplesFolder;
    private static File mComplexExamplesFolder;

    @BeforeClass
    public static void getFilesFromExampleDirectory()
    {
        mSimpleExamplesFolder = new File("examples/SimpleTest");
        mComplexExamplesFolder = new File("examples/ComplexTest");
    }

    @Test
    public void testBreakSimple() throws IOException
    {
        File[] matchingFiles = mSimpleExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("BreakSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testContinueSimple() throws IOException
    {
        File[] matchingFiles = mSimpleExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("ContinueSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testDoWhileSimple() throws IOException
    {
        File[] matchingFiles = mSimpleExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("DoWhileSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testForEachSimple() throws IOException
    {
        File[] matchingFiles = mSimpleExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("ForEachSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testIfElseSimple() throws IOException
    {
        File[] matchingFiles = mSimpleExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("IfElseTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testMethodCallSimple() throws IOException
    {
        File[] matchingFiles = mSimpleExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("MethodCallSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testNestSimple() throws IOException
    {
        File[] matchingFiles = mSimpleExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("NestTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testReturnSimple() throws IOException
    {
        File[] matchingFiles = mSimpleExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("ReturnSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testSwitchSimple() throws IOException
    {
        File[] matchingFiles = mSimpleExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("SwitchSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testThrowSimple() throws IOException
    {
        File[] matchingFiles = mSimpleExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("ThrowSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testTrySimple() throws IOException
    {
        File[] matchingFiles = mSimpleExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("TrySimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testBreakLabelSimple() throws IOException
    {
        File[] matchingFiles = mSimpleExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("BreakLabelSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testForBreakSwitchComplex() throws IOException
    {
        File[] matchingFiles = mComplexExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("ForBreakSwitch");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testForIfElseComplex() throws IOException
    {
        File[] matchingFiles = mComplexExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("ForIfElse");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testLabelIfComplex() throws IOException
    {
        File[] matchingFiles = mComplexExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("LabelIf");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testLoopBreakContinueComplex() throws IOException
    {
        File[] matchingFiles = mComplexExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("LoopBreakContinue");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testWhileTryIfComplex() throws IOException
    {
        File[] matchingFiles = mComplexExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("WhileTryIf");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }
}