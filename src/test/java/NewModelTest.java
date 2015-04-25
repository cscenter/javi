import new_model.ModelTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class NewModelTest {

    private static File mExamplesFolder;

    @BeforeClass
    public static void getFilesFromExampleDirectory()
    {
        mExamplesFolder = new File("examples/SimpleTest");
    }

    @Test
    public void testBreakSimple() throws IOException
    {
        File[] matchingFiles = mExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("BreakSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testContinueSimple() throws IOException
    {
        File[] matchingFiles = mExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("ContinueSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testDoWhileSimple() throws IOException
    {
        File[] matchingFiles = mExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("DoWhileSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testForEachSimple() throws IOException
    {
        File[] matchingFiles = mExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("ForEachSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testIfElseSimple() throws IOException
    {
        File[] matchingFiles = mExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("IfElseTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testMethodCallSimple() throws IOException
    {
        File[] matchingFiles = mExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("MethodCallSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testNestSimple() throws IOException
    {
        File[] matchingFiles = mExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("NestTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testReturnSimple() throws IOException
    {
        File[] matchingFiles = mExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("ReturnSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testSwitchSimple() throws IOException
    {
        File[] matchingFiles = mExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("SwitchSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testThrowSimple() throws IOException
    {
        File[] matchingFiles = mExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("ThrowSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testTrySimple() throws IOException
    {
        File[] matchingFiles = mExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("TrySimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }

    @Test
    public void testWhileSimple() throws IOException
    {
        File[] matchingFiles = mExamplesFolder.listFiles((dir, name) -> {
            return name.startsWith("WhileSimpleTest");
        });
        assertTrue(matchingFiles.length > 0);
        ModelTest.main(new String[]{matchingFiles[0].getAbsolutePath()});
    }
}
