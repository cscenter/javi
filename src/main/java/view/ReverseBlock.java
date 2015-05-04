package view;

import model.Node;

public class ReverseBlock
{
    private Object blockVertex;
    private Node blockNode;
    private int blockCount;
    private double blockY;

    ReverseBlock(Object vertex, Node node, int count)
    {
        this.blockVertex = vertex;
        this.blockNode = node;
        this.blockCount = count;
        this.blockY = 0;
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
}
