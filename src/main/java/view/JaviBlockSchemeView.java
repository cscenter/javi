package view;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import new_model.*;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class JaviBlockSchemeView extends JPanel
{
    private BlockScheme scheme;
    private HashMap<NodeType, String> blockShapes;
    private mxGraph graph;

    private static double initialX = 10.0;
    private static double initialY = 10.0;
    private static double defaultDistance = 60.0;
    private static double defaultWidth = 150.0;

    public JaviBlockSchemeView(BlockScheme scheme)
    {
        this.scheme = scheme;
    }

    public JaviBlockSchemeView()
    {
        this.scheme = new BlockScheme();
    }

    public void setBlock(BlockScheme scheme)
    {
        this.scheme = scheme;
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
            StartNode startNode = scheme.getStart();
            Stack<ReverseBlock> reversalNodes = new Stack<>();
            Object startVertex = vertex(graph, startNode.toString(), startNode.getType(), initialX, initialY);
            mxCell startCell = (mxCell) startVertex;
            if (startCell != null) {
                double vertexHeight = startCell.getGeometry().getHeight();
                constructGraph(graph, startVertex, reversalNodes, initialX, initialY + vertexHeight + defaultDistance, startNode.getNext());
            }
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        add(graphComponent);
    }

    public mxGraph getGraph()
    {
        return graph;
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, Node node)
    {
        if (node == null) {
            if (!reversalNodes.empty()) {
                ReverseBlock reverseBlock = reversalNodes.pop();
                graph.insertEdge(graph.getDefaultParent(), null, null, blockVertex, reverseBlock.getBlockVertex());
                constructGraph(graph, reverseBlock.getBlockVertex(), reversalNodes, x, y, reverseBlock.getNextBlock());
            }
        }
        else if (node instanceof StartNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (StartNode)node);
        }
        else if (node instanceof EndNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (EndNode)node);
        }
        else if (node instanceof ForNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (ForNode)node);
        }
        else if (node instanceof ForEachNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (ForEachNode)node);
        }
        else if (node instanceof IfNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (IfNode)node);
        }
        else if (node instanceof WhileNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (WhileNode)node);
        }
        else if (node instanceof DeclarationNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (DeclarationNode)node);
        }
        else if (node instanceof AssignNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (AssignNode)node);
        }
        else if (node instanceof BinaryNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (BinaryNode)node);
        }
        else if (node instanceof BreakNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (BreakNode)node);
        }
        else if (node instanceof CaseNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (CaseNode)node);
        }
        else if (node instanceof ContinueNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (ContinueNode)node);
        }
        else if (node instanceof MethodCallNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (MethodCallNode)node);
        }
        else if (node instanceof ReturnNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (ReturnNode)node);
        }
        else if (node instanceof SwitchNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (SwitchNode)node);
        }
        else if (node instanceof TryNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (TryNode)node);
        }
        else if (node instanceof CatchNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (CatchNode)node);
        }
        else if (node instanceof UnaryNode) {
            constructGraph(graph, blockVertex, reversalNodes, x, y, (UnaryNode)node);
        }
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, StartNode node)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex);
        mxCell nextCell = (mxCell) nextVertex;
        if (nextCell != null) {
            double vertexHeight = nextCell.getGeometry().getHeight();
            constructGraph(graph, nextVertex, reversalNodes, initialX, initialY + vertexHeight + defaultDistance, node.getNext());
        }
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, EndNode node)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex);
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, ForNode node)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex);

        reversalNodes.push(new ReverseBlock(nextVertex, node));
        mxCell nextCell = (mxCell) nextVertex;
        if (nextCell != null) {
            double vertexHeight = nextCell.getGeometry().getHeight();
            constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, node.getNestedFirst());
        }
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, ForEachNode node)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex);

        reversalNodes.push(new ReverseBlock(nextVertex, node));
        mxCell nextCell = (mxCell) nextVertex;
        if (nextCell != null) {
            double vertexHeight = nextCell.getGeometry().getHeight();
            constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, node.getNestedFirst());
        }
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, IfNode node)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex);

        reversalNodes.push(new ReverseBlock(nextVertex, node));
        mxCell nextCell = (mxCell) nextVertex;
        if (nextCell != null) {
            double vertexWidth = nextCell.getGeometry().getWidth();
            double vertexHeight = nextCell.getGeometry().getHeight();
            constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, node.getYes());
            constructGraph(graph, nextVertex, reversalNodes, x + vertexWidth + defaultDistance, y, node.getNo());
        }
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, WhileNode node)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex);

        reversalNodes.push(new ReverseBlock(nextVertex, node));
        mxCell nextCell = (mxCell) nextVertex;
        if (nextCell != null) {
            double vertexHeight = nextCell.getGeometry().getHeight();
            constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, node.getNestedFirst());
        }
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, DeclarationNode node)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex);
        mxCell nextCell = (mxCell) nextVertex;
        if (nextCell != null) {
            double vertexHeight = nextCell.getGeometry().getHeight();
            constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, node.getNext());
        }
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, AssignNode node)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex);
        mxCell nextCell = (mxCell) nextVertex;
        if (nextCell != null) {
            double vertexHeight = nextCell.getGeometry().getHeight();
            constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, node.getNext());
        }
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, BinaryNode node)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex);
        mxCell nextCell = (mxCell) nextVertex;
        if (nextCell != null) {
            double vertexHeight = nextCell.getGeometry().getHeight();
            constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, node.getNext());
        }
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, BreakNode node)
    {

    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, CaseNode node)
    {

    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, ContinueNode node)
    {

    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, MethodCallNode node)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex);
        mxCell nextCell = (mxCell) nextVertex;
        if (nextCell != null) {
            double vertexHeight = nextCell.getGeometry().getHeight();
            constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, node.getNext());
        }
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, ReturnNode node)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex);
        mxCell nextCell = (mxCell) nextVertex;
        if (nextCell != null) {
            double vertexHeight = nextCell.getGeometry().getHeight();
            constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, node.getNext());
        }
    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, SwitchNode node)
    {

    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, TryNode node)
    {

    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, CatchNode node)
    {

    }

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, UnaryNode node)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex);
        mxCell nextCell = (mxCell) nextVertex;
        if (nextCell != null) {
            double vertexHeight = nextCell.getGeometry().getHeight();
            constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, node.getNext());
        }
    }

    private Object vertex(mxGraph graph, String blockContent, NodeType type, double x, double y)
    {
        Object blockVertex = graph.insertVertex(graph.getDefaultParent(), null, blockContent, x, y, 0, 0, blockShapes.get(type));
        graph.updateCellSize(blockVertex);
        if (blockVertex instanceof mxCell) {
            mxCell cell = (mxCell)blockVertex;
            mxGeometry g = (mxGeometry) cell.getGeometry().clone();
            g.setHeight(g.getHeight() + 20);
            g.setWidth(defaultWidth);
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

        blockShapes.put(NodeType.START, "ellipse");
        blockShapes.put(NodeType.END, "ellipse");
        blockShapes.put(NodeType.FOR, "rhombus");
        blockShapes.put(NodeType.FOREACH, "rhombus");
        blockShapes.put(NodeType.IF, "rhombus");
        blockShapes.put(NodeType.WHILE, "rhombus");

        blockShapes.put(NodeType.DOWHILE, "rhombus");
        blockShapes.put(NodeType.DECLARATION, "rect");
        blockShapes.put(NodeType.ASSIGN, "rect");
        blockShapes.put(NodeType.METHOD, "rect");
        blockShapes.put(NodeType.RETURN, "rect");
        blockShapes.put(NodeType.CONTINUE, "ellipse");

        blockShapes.put(NodeType.BREAK, "ellipse");
        blockShapes.put(NodeType.TRY, "rect");
        blockShapes.put(NodeType.CATCH, "rect");
        blockShapes.put(NodeType.SWITCH, "rect");
        blockShapes.put(NodeType.CASE, "rhombus");
        blockShapes.put(NodeType.UNARY, "rect");
        blockShapes.put(NodeType.BINARY, "rect");
    }
}
