package startDialog;

import com.mxgraph.util.mxCellRenderer;
import reader.JaviParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JaviFrame extends JFrame {
    private JaviSplitPane mJaviSplitPane;

    public JaviFrame() {
        super("Java methods visualization");

        mJaviSplitPane = new JaviSplitPane();
        getContentPane().add(mJaviSplitPane.getSplitPane());
        JToolBar toolBar = new JToolBar();

        String saveToPngKey = "Save to png";
        JButton saveToPngButton = new JButton(saveToPngKey);
        Action saveToPngAction = new AbstractAction(saveToPngKey) {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        };
        saveToPngButton.setAction(saveToPngAction);
        saveToPngAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        saveToPngButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), saveToPngKey);
        saveToPngButton.getActionMap().put(saveToPngKey, saveToPngAction);

        String openKey = "Open";
        JButton openButton = new JButton(openKey);
        Action openAction = new AbstractAction(openKey) {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                        saveToPngButton.setEnabled(true);
                    } else {
                        saveToPngButton.setEnabled(false);
                    }
                }
            }
        };
        openButton.setAction(openAction);
        openAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
        openButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), openKey);
        openButton.getActionMap().put(openKey, openAction);
        toolBar.add(openButton);

        toolBar.add(saveToPngButton);
        saveToPngButton.setEnabled(false);

        JButton exitButton = new JButton("Exit");
        exitButton.setMnemonic(KeyEvent.VK_Q);
        toolBar.add(exitButton);

        exitButton.addActionListener(e -> System.exit(0));
        add(toolBar, BorderLayout.PAGE_START);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }
}