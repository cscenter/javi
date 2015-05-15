package startDialog;

import model.BlockScheme;
import model.BlockSchemeFileBuilder;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import view.JaviBlockSchemeView;

public class JaviSplitPane extends JPanel {
    private JSplitPane mSplitPane;
    private JaviBlockSchemeView mBlockScheme;
    private JTree tree = new JTree();
    private BlockSchemeFileBuilder builderFile;

    public int methodsCount() {
        return builderFile.getBlockScheme().size();
    }

    public void setFileToParse(String path) {
        builderFile = new BlockSchemeFileBuilder(path);
        Map<String, ArrayList<BlockScheme>> mapClassesAndSchemes = builderFile.getBlockScheme();

        String nameFile = path.substring(path.lastIndexOf(File.separator)+1, path.lastIndexOf("."));
        DefaultMutableTreeNode nameFileRoot = new DefaultMutableTreeNode(nameFile);

        for (String key : mapClassesAndSchemes.keySet()) {
            DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(key);
            for (BlockScheme b : mapClassesAndSchemes.get(key)) {
                tmp.add(new DefaultMutableTreeNode(b.getNameMethod()));
            }
            nameFileRoot.add(tmp);
        }
        tree.setModel(new DefaultTreeModel(nameFileRoot));

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);

        for (TreeSelectionListener listener : tree.getTreeSelectionListeners()) {
            tree.removeTreeSelectionListener(listener);
        }

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                String currentClick = e.getPath().getLastPathComponent().toString();
                if (!mapClassesAndSchemes.keySet().contains(currentClick)) {
                    for (String key : mapClassesAndSchemes.keySet()) {
                        if (key.equals(e.getPath().getPath()[e.getPath().getPath().length - 2].toString())) {
                            for (BlockScheme bs : mapClassesAndSchemes.get(key)) {
                                if (bs.getNameMethod().equals(currentClick)) {
                                    mBlockScheme.removeAll();
                                    mBlockScheme.updateUI();
                                    mBlockScheme.setBlock(bs);
                                    mBlockScheme.repaint();
                                    mBlockScheme.revalidate();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public JaviSplitPane() {
        tree.setModel(null);
        JScrollPane listScrollPane = new JScrollPane(tree);

        mBlockScheme = new JaviBlockSchemeView();
        JScrollPane blockSchemeScrollPane = new JScrollPane(mBlockScheme);

        mSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                listScrollPane, blockSchemeScrollPane);
        mSplitPane.setOneTouchExpandable(true);
        mSplitPane.setDividerLocation(150);

        Dimension minimumSize = new Dimension(100, 50);
        listScrollPane.setMinimumSize(minimumSize);
        blockSchemeScrollPane.setMinimumSize(minimumSize);
        mSplitPane.setResizeWeight(0.1);
    }

    public JSplitPane getSplitPane() {
        return mSplitPane;
    }

    public JaviBlockSchemeView getmBlockScheme() {
        return mBlockScheme;
    }
}