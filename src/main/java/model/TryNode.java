package model;

import com.github.antlrjavaparser.api.stmt.TryStmt;

import java.util.ArrayList;

public class TryNode extends Node{
    private Node nestedFirst;
    private String expTry;
    ArrayList<CatchNode> catchClause = new ArrayList<CatchNode>();

    public FinallyNode getFinallyBlock() {
        return finallyBlock;
    }

    private FinallyNode finallyBlock;

    public TryNode(TryStmt node) {
        super(node);
        this.expTry = "try:";
        if (node.getFinallyBlock() != null) {
            this.finallyBlock = new FinallyNode(node.getFinallyBlock());
            finallyBlock.level = level + 1;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = this.nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("try {\n");
        while (tmp != null) {
            builder.append(tmp);
            tmp = tmp.next;
        }

        if (this.catchClause != null) {
            for (CatchNode catchNode : catchClause) {
                builder.append(catchNode.toString());
            }
        } else {
            builder.append("} ");
        }
        if (this.finallyBlock != null) {
            builder.append(finallyBlock.toString());
        }

        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.TRY;
    }

    @Override
    public void attachInner(Node node) {
        if (nestedFirst == null) {
            nestedFirst = node;
            node.level = this.level + 1;
        } else {
            assert node.getType() == NodeType.CATCH;
            CatchNode catchNode = (CatchNode)node;
            catchClause.add(catchNode);
            node.level = this.level;
        }
    }

    public ArrayList<CatchNode> getCatchClause()
    {
        return catchClause;
    }

    public Node getNestedFirst()
    {
        return nestedFirst;
    }
}
