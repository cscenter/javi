package model;

import com.github.antlrjavaparser.api.stmt.LabeledStmt;

public class LabelNode extends Node {
    private String label;
    private Node nestedFirst;

    public String getLabel() {
        return label;
    }

    public LabelNode(LabeledStmt node) {
        super(node);
        label = node.getLabel();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append(label).append(":").append("\n");

        while (tmp != null && tmp != this) {
            builder.append(tmp.toString());
            tmp = tmp.next;
        }

        return builder.toString();
    }

    @Override
    public void setNext(Node node) {
        super.setNext(node);
        nestedFirst.setNext(node);
    }

    @Override
    public NodeType getType() {
        return NodeType.LABEL;
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
    }

    public Node getNestedFirst()
    {
        return nestedFirst;
    }
}