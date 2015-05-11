package model;

import com.github.antlrjavaparser.api.stmt.ContinueStmt;

public class ContinueNode extends Node {
    private String exp;
    private String label;

    public String getExp() {
        return exp;
    }

    public String getLabel() {
        return label;
    }

    public ContinueNode(ContinueStmt node) {
        super(node);
        label = node.getId();
        this.exp = "continue " + (label == null ? "" : label);
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
        return NodeType.CONTINUE;
    }

    @Override
    public void attachInner(Node node) {
    }
}