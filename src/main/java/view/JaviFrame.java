package view;

import model.JaviParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Anastasiia Kuzenkova on 10.03.15.
 */
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
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser openFile = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "java source files", "java");
                openFile.setFileFilter(filter);
                int returnVal = openFile.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = openFile.getSelectedFile();
                    mJaviSplitPane.setListData(JaviParser.methodsByFile(file));
                }
            }
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

}
