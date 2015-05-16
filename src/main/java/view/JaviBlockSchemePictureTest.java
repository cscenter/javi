package view;

import com.mxgraph.util.mxCellRenderer;
import model.BlockScheme;
import reader.JaviParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JaviBlockSchemePictureTest
{
    public static void saveToPng(String[] args)
    {
        File classFile = new File(args[0]);
        JaviParser parser = new JaviParser(classFile);
        String className = args[1];
        saveToPngByClassName(parser, classFile, className);
    }

    public static void saveAll()
    {
        ArrayList<File> javaExampleFiles = javaFiles("examples/SimpleTest");
        for (File javaFile : javaExampleFiles) {
            JaviParser parser = new JaviParser(javaFile);
            if (!javaFile.getName().equals("SwitchSimpleTest.java")) {
                for (String className : parser.methodNames()) {
                    saveToPngByClassName(parser, javaFile, className);
                }
            }
        }
    }

    private static void saveToPngByClassName(JaviParser parser, File classFile, String className)
    {
        BlockScheme blockScheme = parser.getMethodBodyByName(className);
        JaviBlockSchemeView view = new JaviBlockSchemeView();
        view.setBlock(blockScheme);
        File pngFile = new File(classFile.getName().replaceFirst("[.][^.]+$", "") + "_" + className + ".png");
        BufferedImage image = mxCellRenderer.createBufferedImage(view.getGraph(), null, 1, Color.WHITE, true, null);
        try {
            ImageIO.write(image, "PNG", pngFile);
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static ArrayList<File> javaFiles(String directoryName) {
        File dir = new File(directoryName);
        ArrayList<File> resultList = new ArrayList<>();
        File[] fList = dir.listFiles();
        for (File file: fList) {
            if (file.isFile()) {
                String extension = "";
                int i = file.getName().lastIndexOf('.');
                if (i > 0) {
                    extension = file.getName().substring(i + 1);
                    if (extension.equals("java")) {
                        resultList.add(file);
                    }
                }
            }
            else if (file.isDirectory()) {
                resultList.addAll(javaFiles(file.getAbsolutePath()));
            }
        }
        return resultList;
    }
}
