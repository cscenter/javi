package view;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;

public class JaviBlockSchemeView extends JPanel
{

    private JaviBlockSchemeBlock block;

    public JaviBlockSchemeView(JaviBlockSchemeBlock block)
    {
        this.block = block;
    }

    public JaviBlockSchemeView()
    {
        this.block = new JaviBlockSchemeBlock();
    }

    public void setBlock(JaviBlockSchemeBlock block)
    {
        if (this.block == block) {
            return;
        }
        this.block = block;
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,
                    30);
            Object v2 = graph.insertVertex(parent, null, "World!", 240, 150,
                    80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        add(graphComponent);
    }
}
