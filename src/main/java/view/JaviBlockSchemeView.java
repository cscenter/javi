package view;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import model.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class JaviBlockSchemeView extends JPanel
{
    private enum JaviBlockSchemeEdgeStyle
    {
        JaviBlockSchemeEdgeStyleLeftLeft,
        JaviBlockSchemeEdgeStyleBottomTop,
        JaviBlockSchemeEdgeStyleRightRight,
        JaviBlockSchemeEdgeStyleRightTop,
        JaviBlockSchemeEdgeStyleLeftTop,
        JaviBlockSchemeEdgeStyleRightLeft
    }

    private BlockScheme scheme;
    private HashMap<NodeType, String> blockShapes;
    private HashMap<JaviBlockSchemeEdgeStyle, String> edgeShapes;
    private mxGraph graph;
    private Object commonEndVertex;
    private ArrayList<Object> toEndVertex;

    private static double initialX = 10.0;
    private static double initialY = 10.0;
    private static double defaultDistance = 60.0;

    public JaviBlockSchemeView()
    {
        this.scheme = new BlockScheme();
        this.toEndVertex = new ArrayList<>();
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

        constructEdgeStyles(graph);
        constructVertexStyles(graph);
        toEndVertex.clear();

        graph.getModel().beginUpdate();
        try
        {
            StartNode startNode = scheme.getStart();
            Stack<ReverseBlock> reversalNodes = new Stack<>();
            Object startVertex = vertex(graph, startNode.toString(), startNode.getType(), initialX, initialY);
            mxCell startCell = (mxCell) startVertex;
            if (startCell != null) {
                double vertexHeight = startCell.getGeometry().getHeight();
                constructGraph(graph, startVertex, reversalNodes, initialX, initialY + vertexHeight + defaultDistance, startNode.getNext(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleBottomTop);
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

    private void constructGraph(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, Node node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        if (node == null) {
            if (!reversalNodes.empty()) {
                ReverseBlock reverseBlock = reversalNodes.peek();
                if (reverseBlock.getBlockNeedReverseLink()) {
                    graph.insertEdge(graph.getDefaultParent(), null, null, blockVertex, reverseBlock.getBlockVertex(), edgeShapes.get(JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleRightRight));
                }
                else {
                    toEndVertex.add(blockVertex);
                }
                if (reverseBlock.shouldGoToNext(y)) {
                    reversalNodes.pop();
                    constructGraph(graph, reverseBlock.getBlockVertex(), reversalNodes, x, reverseBlock.getBlockY(), reverseBlock.getNextBlock(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleLeftLeft);
                }
            }
            else {
               toEndVertex.add(blockVertex);
            }
        }
        else if (node instanceof EndNode) {
            /*if (!reversalNodes.empty()) {
                ReverseBlock reverseBlock = reversalNodes.peek();
                graph.insertEdge(graph.getDefaultParent(), null, null, blockVertex, reverseBlock.getBlockVertex(), edgeShapes.get(JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleRightRight));
                if (reverseBlock.shouldGoToNext(y)) {
                    reversalNodes.pop();
                    constructGraph(graph, reverseBlock.getBlockVertex(), reversalNodes, x, reverseBlock.getBlockY(), reverseBlock.getNextBlock(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleLeftLeft);
                }
            }*/
            endVertex(graph, blockVertex, reversalNodes, x, y, (EndNode)node, edgeStyle);
        }
        else if (node instanceof ForNode) {
            ForNode forNode = (ForNode)node;
            Object nextVertex = forVertex(graph, blockVertex, reversalNodes, x, y, forNode, edgeStyle);
            mxCell nextCell = (mxCell) nextVertex;
            if (nextCell != null) {
                double vertexHeight = nextCell.getGeometry().getHeight();
                constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, forNode.getNestedFirst(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleBottomTop);
            }
        }
        else if (node instanceof ForEachNode) {
            ForEachNode forEachNode = (ForEachNode)node;
            Object nextVertex = foreachVertex(graph, blockVertex, reversalNodes, x, y, forEachNode, edgeStyle);
            mxCell nextCell = (mxCell) nextVertex;
            if (nextCell != null) {
                double vertexHeight = nextCell.getGeometry().getHeight();
                constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, forEachNode.getNestedFirst(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleBottomTop);
            }
        }
        else if (node instanceof IfNode) {
            IfNode ifNode = (IfNode)node;
            Object nextVertex = ifVertex(graph, blockVertex, reversalNodes, x, y, ifNode, edgeStyle);
            mxCell nextCell = (mxCell) nextVertex;
            if (nextCell != null) {
                double vertexWidth = nextCell.getGeometry().getWidth();
                double vertexHeight = nextCell.getGeometry().getHeight();
                constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, ifNode.getYes(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleBottomTop);
                if (ifNode.getNo() != null) {
                    constructGraph(graph, nextVertex, reversalNodes, x + vertexWidth + defaultDistance, y, ifNode.getNo(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleRightLeft);
                }
            }
        }
        else if (node instanceof WhileNode) {
            WhileNode whileNode = (WhileNode)node;
            Object nextVertex = whileVertex(graph, blockVertex, reversalNodes, x, y, whileNode, edgeStyle);

            mxCell nextCell = (mxCell) nextVertex;
            if (nextCell != null) {
                double vertexHeight = nextCell.getGeometry().getHeight();
                constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, whileNode.getNestedFirst(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleBottomTop);
            }
        }
        else if (node instanceof DeclarationNode) {
            DeclarationNode declarationNode = (DeclarationNode)node;
            Object nextVertex = declarationVertex(graph, blockVertex, reversalNodes, x, y, declarationNode, edgeStyle);
            mxCell nextCell = (mxCell) nextVertex;
            if (nextCell != null) {
                double vertexHeight = nextCell.getGeometry().getHeight();
                constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, declarationNode.getNext(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleBottomTop);
            }
        }
        else if (node instanceof AssignNode) {
            AssignNode assignNode = (AssignNode)node;
            Object nextVertex = assignVertex(graph, blockVertex, reversalNodes, x, y, assignNode, edgeStyle);
            mxCell nextCell = (mxCell) nextVertex;
            if (nextCell != null) {
                double vertexHeight = nextCell.getGeometry().getHeight();
                constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, assignNode.getNext(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleBottomTop);
            }
        }
        else if (node instanceof BinaryNode) {
            BinaryNode binaryNode = (BinaryNode)node;
            Object nextVertex = binaryVertex(graph, blockVertex, reversalNodes, x, y, binaryNode, edgeStyle);
            mxCell nextCell = (mxCell) nextVertex;
            if (nextCell != null) {
                double vertexHeight = nextCell.getGeometry().getHeight();
                constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, binaryNode.getNext(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleBottomTop);
            }
        }
        else if (node instanceof BreakNode) {
        }
        else if (node instanceof CaseNode) {
        }
        else if (node instanceof ContinueNode) {
        }
        else if (node instanceof MethodCallNode) {
            MethodCallNode methodCallNode = (MethodCallNode)node;
            Object nextVertex = methodCallVertex(graph, blockVertex, reversalNodes, x, y, methodCallNode, edgeStyle);
            mxCell nextCell = (mxCell) nextVertex;
            if (nextCell != null) {
                double vertexHeight = nextCell.getGeometry().getHeight();
                constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, methodCallNode.getNext(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleBottomTop);
            }
        }
        else if (node instanceof ReturnNode) {
            ReturnNode returnNode = (ReturnNode)node;
            Object nextVertex = returnVertex(graph, blockVertex, reversalNodes, x, y, returnNode, edgeStyle);
            mxCell nextCell = (mxCell) nextVertex;
            if (nextCell != null) {
                double vertexHeight = nextCell.getGeometry().getHeight();
                constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, returnNode.getNext(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleBottomTop);
            }
        }
        else if (node instanceof SwitchNode) {
        }
        else if (node instanceof TryNode) {
        }
        else if (node instanceof CatchNode) {
        }
        else if (node instanceof ThrowNode) {
        }
        else if (node instanceof LabelNode) {
        }
        else if (node instanceof FinallyNode) {
        }
        else if (node instanceof UnaryNode) {
            UnaryNode unaryNode = (UnaryNode)node;
            Object nextVertex = unaryVertex(graph, blockVertex, reversalNodes, x, y, unaryNode, edgeStyle);
            mxCell nextCell = (mxCell) nextVertex;
            if (nextCell != null) {
                double vertexHeight = nextCell.getGeometry().getHeight();
                constructGraph(graph, nextVertex, reversalNodes, x, y + vertexHeight + defaultDistance, unaryNode.getNext(), JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleBottomTop);
            }
        }
    }

    private void endVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, EndNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        Object nextVertex = vertex(graph, node.toString(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex, edgeShapes.get(edgeStyle));
        for (Object to : toEndVertex) {
            graph.insertEdge(graph.getDefaultParent(), null, "", to, nextVertex, edgeShapes.get(edgeStyle));
        }
    }

    private Object forVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, ForNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        Object nextVertex = vertex(graph, node.getCondition(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex, edgeShapes.get(edgeStyle));
        reversalNodes.push(new ReverseBlock(nextVertex, node, 1, true));

        return nextVertex;
    }

    private Object foreachVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, ForEachNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        Object nextVertex = vertex(graph, node.getCondition(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex, edgeShapes.get(edgeStyle));
        reversalNodes.push(new ReverseBlock(nextVertex, node, 1, true));

        return nextVertex;
    }

    private Object ifVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, IfNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        Object nextVertex = vertex(graph, node.getCondition(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex, edgeShapes.get(edgeStyle));
        reversalNodes.push(new ReverseBlock(nextVertex, node, (node.getNo() != null ? 2 : 1), false));

        return nextVertex;
    }

    private Object whileVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, WhileNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        Object nextVertex = vertex(graph, node.getCondition(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex, edgeShapes.get(edgeStyle));
        reversalNodes.push(new ReverseBlock(nextVertex, node, 1, true));

        return nextVertex;
    }

    private Object declarationVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, DeclarationNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        Object nextVertex = vertex(graph, node.getExp(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex, edgeShapes.get(edgeStyle));
        return nextVertex;
    }

    private Object assignVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, AssignNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        Object nextVertex = vertex(graph, node.getExp(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex, edgeShapes.get(edgeStyle));
        return nextVertex;
    }

    private Object binaryVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, BinaryNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        Object nextVertex = vertex(graph, node.getExp(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex, edgeShapes.get(edgeStyle));
        return nextVertex;
    }

    private Object breakVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, BreakNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        return null;
    }

    private Object caseVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, CaseNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        return null;
    }

    private Object continueVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, ContinueNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        return null;
    }

    private Object methodCallVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, MethodCallNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        Object nextVertex = vertex(graph, node.getMethod(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex, edgeShapes.get(edgeStyle));
        return nextVertex;
    }

    private Object returnVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, ReturnNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        Object nextVertex = vertex(graph, node.getExpression(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex, edgeShapes.get(edgeStyle));
        return nextVertex;
    }

    private Object switchVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, SwitchNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        Object nextVertex = vertex(graph, node.getSelector(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex, edgeShapes.get(edgeStyle));
        reversalNodes.push(new ReverseBlock(nextVertex, node, node.getEntries().size(), false));
        return nextVertex;
    }

    private Object tryVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, TryNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        return null;
    }

    private Object catchVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, CatchNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        return null;
    }

    private Object throwVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, ThrowNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        return null;
    }

    private Object labelVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, LabelNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        return null;
    }

    private Object finallyVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, FinallyNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        return null;
    }

    private Object unaryVertex(mxGraph graph, Object blockVertex, Stack<ReverseBlock>reversalNodes, double x, double y, UnaryNode node, JaviBlockSchemeEdgeStyle edgeStyle)
    {
        Object nextVertex = vertex(graph, node.getExp(), node.getType(), x, y);
        graph.insertEdge(graph.getDefaultParent(), null, "", blockVertex, nextVertex, edgeShapes.get(edgeStyle));
        return nextVertex;
    }

    private Object vertex(mxGraph graph, String blockContent, NodeType type, double x, double y)
    {
        Object blockVertex = graph.insertVertex(graph.getDefaultParent(), null, blockContent, x, y, 0, 0, blockShapes.get(type));
        graph.updateCellSize(blockVertex);
        if (blockVertex instanceof mxCell) {
            mxCell cell = (mxCell)blockVertex;
            mxGeometry g = (mxGeometry) cell.getGeometry().clone();
            g.setHeight(g.getHeight() + 20);
            double defaultWidth = 250.0;
            g.setWidth(defaultWidth);
            graph.cellsResized(new Object[] {cell}, new mxRectangle[] {g});
        }
        return blockVertex;
    }

    private void constructEdgeStyles(mxGraph graph)
    {
        edgeShapes = new HashMap<>();

        constructEdgeStyle("0", "0.5", "0", "0.5", "left-left", JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleLeftLeft, graph);
        constructEdgeStyle("0.5", "0", "0.5", "1", "bottom-top", JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleBottomTop, graph);
        constructEdgeStyle("0.5", "0", "0", "0.5", "left-top", JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleLeftTop, graph);
        constructEdgeStyle("1", "0.5", "1", "0.5", "right-right", JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleRightRight, graph);
        constructEdgeStyle("0.5", "0", "1", "0.5", "right-top", JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleRightTop, graph);
        constructEdgeStyle("0", "0.5", "1", "0.5", "right-left", JaviBlockSchemeEdgeStyle.JaviBlockSchemeEdgeStyleRightLeft, graph);
    }

    private void constructVertexStyles(mxGraph graph)
    {
        Map<String, Object> vertexRhombus = defaultVertexStyle();
        vertexRhombus.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RHOMBUS);

        Map<String, Object> vertexRect = defaultVertexStyle();
        vertexRect.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);

        Map<String, Object> vertexEllipse = defaultVertexStyle();
        vertexEllipse.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);

        Map<String, Object> vertexHexagon = defaultVertexStyle();
        vertexEllipse.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_HEXAGON);

        graph.getStylesheet().putCellStyle("rhombus", vertexRhombus);
        graph.getStylesheet().putCellStyle("rect", vertexRect);
        graph.getStylesheet().putCellStyle("ellipse", vertexEllipse);
        graph.getStylesheet().putCellStyle("hexagon", vertexHexagon);

        blockShapes = new HashMap<>();

        blockShapes.put(NodeType.START, "ellipse");
        blockShapes.put(NodeType.END, "ellipse");
        blockShapes.put(NodeType.FOR, "hexagon");
        blockShapes.put(NodeType.FOREACH, "hexagon");
        blockShapes.put(NodeType.IF, "rhombus");
        blockShapes.put(NodeType.WHILE, "hexagon");

        blockShapes.put(NodeType.DOWHILE, "hexagon");
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
        blockShapes.put(NodeType.THROW, "ellipse");
        blockShapes.put(NodeType.LABEL, "ellipse");
        blockShapes.put(NodeType.FINALLY, "ellipse");
    }

    private Map<String, Object> defaultVertexStyle()
    {
        Map<String, Object> vertexStyle = new HashMap<>();
        vertexStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "#ffffff");
        vertexStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");

        return vertexStyle;
    }

    private void constructEdgeStyle(String styleEntryX, String styleEntryY,
                                                 String styleExitX, String styleExitY, String styleName,
                                                 JaviBlockSchemeEdgeStyle edgeStyle, mxGraph graph)
    {
        Map<String, Object> edgeStyleMap = new HashMap<>();
        edgeStyleMap.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);
        edgeStyleMap.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        edgeStyleMap.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        edgeStyleMap.put(mxConstants.STYLE_STROKECOLOR, "black");
        edgeStyleMap.put(mxConstants.STYLE_ENTRY_X, styleEntryX);
        edgeStyleMap.put(mxConstants.STYLE_ENTRY_Y, styleEntryY);
        edgeStyleMap.put(mxConstants.STYLE_EXIT_X, styleExitX);
        edgeStyleMap.put(mxConstants.STYLE_EXIT_Y, styleExitY);

        graph.getStylesheet().putCellStyle(styleName, edgeStyleMap);
        edgeShapes.put(edgeStyle, styleName);
    }

}