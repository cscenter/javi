package view;

import model.Node;

import java.util.ArrayList;

public class ReverseBlock
{
    private Object blockVertex;
    private Node blockNode;
    private int blockCount;
    private double blockY;
    private boolean blockNeedReverseLink;
    private ArrayList<Object> blockToNextVertexes;

    ReverseBlock(Object vertex, Node node, int count, boolean needReverseLink)
    {
        this.blockVertex = vertex;
        this.blockNode = node;
        this.blockCount = count;
        this.blockY = 0;
        this.blockNeedReverseLink = needReverseLink;
        this.blockToNextVertexes = new ArrayList<>();
    }

    Node getNextBlock()
    {
        return blockNode.getNext();
    }

    Object getBlockVertex()
    {
        return blockVertex;
    }

    boolean shouldGoToNext(double y)
    {
        blockCount--;
        if (blockY < y) {
            blockY = y;
        }
        return blockCount <= 0;
    }

    double getBlockY()
    {
        return blockY;
    }

    boolean getBlockNeedReverseLink()
    {
        return blockNeedReverseLink;
    }

    void addAllToNextVertex(ArrayList<Object> blockVertexes)
    {
        blockToNextVertexes.addAll(blockVertexes);
    }

    ArrayList<Object> getBlockToNextVertexes()
    {
        return blockToNextVertexes;
    }
}
