package view;

import new_model.Node;

public class ReverseBlock
{
    private Object blockVertex;
    private Node blockNode;

    ReverseBlock(Object vertex, Node node) {
        this.blockVertex = vertex;
        this.blockNode = node;
    }

    Node getNextBlock()
    {
        return blockNode.getNext();
    }

    Object getBlockVertex()
    {
        return blockVertex;
    }
}
