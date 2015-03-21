package startDialog;

import reader.JaviParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class JaviFrame extends JFrame {
    private JaviSplitPane mJaviSplitPane;

    public JaviFrame() {
        super("Java methods visualization");

        mJaviSplitPane = new JaviSplitPane();
        getContentPane().add(mJaviSplitPane.getSplitPane());
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        openItem.addActionListener(e -> {
            JFileChooser openFile = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "java source files", "java");
            openFile.setFileFilter(filter);
            int returnVal = openFile.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = openFile.getSelectedFile();
                JaviParser parser = new JaviParser(file);
                mJaviSplitPane.setListData(parser);
            }
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);

        exitItem.addActionListener(e -> System.exit(0));

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
