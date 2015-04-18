package view;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class JaviBlockSchemeView extends JPanel
{

    private JaviBlockSchemeBlock block;
    private HashMap<JaviBlockSchemeBlockType, String> blockShapes;
    private mxGraph graph;

    public JaviBlockSchemeView(JaviBlockSchemeBlock block)
    {
        this.block = block;
    }

    public JaviBlockSchemeView()
    {
        this.block = new JaviBlockSchemeBlock(JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeMethod, "", 0);
    }

    public void setBlock(JaviBlockSchemeBlock block)
    {
        this.block = block;
        graph = new mxGraph();
        graph.setAllowDanglingEdges(false);
        graph.setCellsEditable(false);
        graph.setCellsSelectable(false);
        graph.setCellsBendable(false);

        graph.setAutoSizeCells(true);
        graph.setCellsResizable(true);

        applyEdgeDefaults(graph);
        constructVertexStyles(graph);

        graph.getModel().beginUpdate();
        try
        {
            Object blockVertex = vertex(graph, block.getContent(), block.getType());
            constructGraph(graph, blockVertex, block);
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        add(graphComponent);
        layoutGraph(graph);
    }

    public mxGraph getGraph()
    {
        return graph;
    }

    private void constructGraph(mxGraph graph, Object blockVertex, JaviBlockSchemeBlock block)
    {
        int n = block.getNestedStatements().size();
        for (int i = 0; i < n; ++i) {
            Object previousVertex = blockVertex;
            for (JaviBlockSchemeBlock javiBlock : block.getNestedStatements().get(i)) {
                Object nestedVertex = vertex(graph, javiBlock.getContent(), javiBlock.getType());
                graph.insertEdge(graph.getDefaultParent(), null, "", previousVertex, nestedVertex);
                constructGraph(graph, nestedVertex, javiBlock);
                previousVertex = nestedVertex;
            }
        }
    }

    private void layoutGraph(mxGraph graph)
    {
        mxGraphLayout layout = new mxHierarchicalLayout(graph);
        Object cell = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            layout.execute(cell);
        } finally {
            graph.getModel().endUpdate();
        }
    }

    private Object vertex(mxGraph graph, String blockContent, JaviBlockSchemeBlockType type)
    {
        Object blockVertex = graph.insertVertex(graph.getDefaultParent(), null, blockContent, 0, 0, 0, 0, blockShapes.get(type));
        graph.updateCellSize(blockVertex);
        if (blockVertex instanceof mxCell) {
            mxCell cell = (mxCell)blockVertex;
            mxGeometry g = (mxGeometry) cell.getGeometry().clone();
            g.setHeight(g.getHeight() + 20);
            g.setWidth(g.getWidth() + 40);
            graph.cellsResized(new Object[] {cell}, new mxRectangle[] {g});
        }
        return blockVertex;
    }

    private void applyEdgeDefaults(mxGraph graph)
    {
        Map<String, Object> edge = new HashMap<>();
        edge.put(mxConstants.STYLE_ROUNDED, false);
        edge.put(mxConstants.STYLE_ORTHOGONAL, true);
        edge.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle");
        edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        edge.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        edge.put(mxConstants.STYLE_FONTCOLOR, "#446299");

        mxStylesheet edgeStyle = new mxStylesheet();
        edgeStyle.setDefaultEdgeStyle(edge);
        graph.setStylesheet(edgeStyle);
    }

    private void constructVertexStyles(mxGraph graph)
    {
        Map<String, Object> vertexRhombus = new HashMap<>();
        vertexRhombus.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        vertexRhombus.put(mxConstants.STYLE_FILLCOLOR, "#ffffff");
        vertexRhombus.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        vertexRhombus.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RHOMBUS);

        Map<String, Object> vertexRect = new HashMap<>();
        vertexRect.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        vertexRect.put(mxConstants.STYLE_FILLCOLOR, "#ffffff");
        vertexRect.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        vertexRect.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);

        Map<String, Object> vertexEllipse = new HashMap<>();
        vertexEllipse.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        vertexEllipse.put(mxConstants.STYLE_FILLCOLOR, "#ffffff");
        vertexEllipse.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        vertexEllipse.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);

        graph.getStylesheet().putCellStyle("rhombus", vertexRhombus);
        graph.getStylesheet().putCellStyle("rect", vertexRect);
        graph.getStylesheet().putCellStyle("ellipse", vertexEllipse);

        blockShapes = new HashMap<>();
        blockShapes.put(JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeExpression, "rect");
        blockShapes.put(JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeForStatement, "rhombus");
        blockShapes.put(JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeReturn, "rect");
        blockShapes.put(JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeThrow, "rect");
        blockShapes.put(JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeIfStatement, "rhombus");
        blockShapes.put(JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeMethod, "ellipse");
    }
}
