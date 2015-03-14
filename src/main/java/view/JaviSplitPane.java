package view;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Anastasiia Kuzenkova on 10.03.15.
 */
public class JaviSplitPane extends JPanel implements ListSelectionListener
{
    private JList mMethodsList;
    private JSplitPane mSplitPane;
    private JLabel mBlockScheme;

    public JaviSplitPane()
    {
        mMethodsList = new JList();
        mMethodsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mMethodsList.addListSelectionListener(this);

        JScrollPane listScrollPane = new JScrollPane(mMethodsList);
        mBlockScheme = new JLabel();
        mBlockScheme.setHorizontalAlignment(JLabel.CENTER);

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

    public void setListData(ArrayList<String> listData)
    {
        mMethodsList.setListData(listData.toArray());
        if (!listData.isEmpty()) {
            mMethodsList.setSelectedIndex(0);
        }
    }

    protected void updateLabel(String name)
    {
        ImageIcon icon = new ImageIcon();
        mBlockScheme.setIcon(icon);
        if (icon != null) {
            mBlockScheme.setText(null);
            } else {
            mBlockScheme.setText("Image not found");
        }
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
