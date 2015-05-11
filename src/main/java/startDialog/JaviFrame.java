package startDialog;

import com.mxgraph.util.mxCellRenderer;
import reader.JaviParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JaviFrame extends JFrame {
    private JaviSplitPane mJaviSplitPane;

    public JaviFrame() {
        super("Java methods visualization");

        mJaviSplitPane = new JaviSplitPane();
        getContentPane().add(mJaviSplitPane.getSplitPane());
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem saveToPngItem = new JMenuItem("Save to png");
        fileMenu.add(saveToPngItem);
        saveToPngItem.addActionListener(e -> {
            JFileChooser saveFile = new JFileChooser();
            saveFile.setCurrentDirectory(new File("."));
            int returnVal = saveFile.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                BufferedImage image = mxCellRenderer.createBufferedImage(mJaviSplitPane.getmBlockScheme().getGraph(), null, 1, Color.WHITE, true, null);
                try {
                    ImageIO.write(image, "PNG", saveFile.getSelectedFile());
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        openItem.addActionListener(e -> {
            JFileChooser openFile = new JFileChooser();
            openFile.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "java source files", "java");
            openFile.setFileFilter(filter);
            int returnVal = openFile.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = openFile.getSelectedFile();
                JaviParser parser = new JaviParser(file);
                mJaviSplitPane.setListData(parser);
                if (parser.methodNames().length > 0) {
                    saveToPngItem.setEnabled(true);
                }
                else {
                    saveToPngItem.setEnabled(false);
                }
            }
        });

        fileMenu.add(saveToPngItem);
        saveToPngItem.setEnabled(false);

        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);

        exitItem.addActionListener(e -> System.exit(0));

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }
}