package startDialog;

import reader.JaviParser;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import view.JaviBlockSchemeView;

public class JaviSplitPane extends JPanel implements ListSelectionListener
{
    private JList mMethodsList;
    private JSplitPane mSplitPane;
    private JaviBlockSchemeView mBlockScheme;
    private JaviParser mJaviParser;

    public JaviSplitPane()
    {
        mMethodsList = new JList();
        mMethodsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mMethodsList.addListSelectionListener(this);

        JScrollPane listScrollPane = new JScrollPane(mMethodsList);
        mBlockScheme = new JaviBlockSchemeView();

        JScrollPane blockSchemeScrollPane = new JScrollPane(mBlockScheme);

        mSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                listScrollPane, blockSchemeScrollPane);
        mSplitPane.setOneTouchExpandable(true);
        mSplitPane.setDividerLocation(150);

        Dimension minimumSize = new Dimension(100, 50);
        listScrollPane.setMinimumSize(minimumSize);
        blockSchemeScrollPane.setMinimumSize(minimumSize);

        mSplitPane.setPreferredSize(new Dimension(400, 200));
    }

    public JSplitPane getSplitPane()
    {
        return mSplitPane;
    }

    public void setListData(JaviParser parser)
    {
        mJaviParser = parser;
        String [] listData = mJaviParser.methodNames();
        mMethodsList.setListData(listData);
        if (listData.length > 0) {
            mMethodsList.setSelectedIndex(0);
            updateLabel((String)mMethodsList.getSelectedValue());
        }
    }

    protected void updateLabel(String name)
    {
        if (name == null) {
            return;
        }
        mBlockScheme.removeAll();
        mBlockScheme.updateUI();
        mBlockScheme.setBlock(mJaviParser.getMethodBodyByName(name));
        mBlockScheme.repaint();
        mBlockScheme.revalidate();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList)e.getSource();
        updateLabel((String)list.getSelectedValue());
    }
    public JaviBlockSchemeView getmBlockScheme()
    {
        return mBlockScheme;
    }
}
