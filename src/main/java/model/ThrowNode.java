package model;

import com.github.antlrjavaparser.api.stmt.ThrowStmt;

public class ThrowNode extends Node {
    private String exp;

    public ThrowNode(ThrowStmt node) {
        super(node);
        this.exp = "throw " + node.getExpr().toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.THROW;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(exp).append("\n");
        return builder.toString();
    }

    @Override
    public void attachInner(Node node) {
    }
}
