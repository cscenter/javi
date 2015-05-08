package model;

import com.github.antlrjavaparser.api.stmt.WhileStmt;

public class WhileNode extends Node {
    private Node nestedFirst;
    private String condition;

    public WhileNode(WhileStmt node) {
        super(node);
        this.condition = node.getCondition().toString();
    }

    public Node getNestedFirst()
    {
        return nestedFirst;
    }

    public String getCondition()
    {
        return condition;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("while (").append(condition).append(") {\n");
        while (tmp != null && tmp != this) {
            builder.append(tmp.toString());
            tmp = tmp.next;
        }

        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append("}\n");
        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.WHILE;
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
    }
}