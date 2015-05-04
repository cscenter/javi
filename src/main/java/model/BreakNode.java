package model;

import com.github.antlrjavaparser.api.stmt.BreakStmt;

public class BreakNode extends Node {
    private String exp;
    private String label;

    public String getLabel() {
        return label;
    }

    public String getExp() {
        return exp;
    }

    public BreakNode(BreakStmt node) {
        super(node);
        label = node.getId();
        this.exp = "break " + (label == null ? "" : label);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(exp).append("\n");
        return  builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.BREAK;
    }

    @Override
    public void attachInner(Node node) {

    }
}
