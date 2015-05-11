package model;

import com.github.antlrjavaparser.api.stmt.ForeachStmt;

public class ForEachNode extends Node {
    private Node nestedFirst;
    private String condition;

    public ForEachNode(ForeachStmt node) {
        super(node);
        String iterable = "";
        String variable = "";
        if (node.getIterable() != null) {
            iterable = node.getIterable().toString();
        }
        if (node.getVariable() != null) {
            variable = node.getVariable().toString();
        }
        this.condition = variable + " : " + iterable;
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

        builder.append("foreach (").append(condition).append(") {\n");
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
        return NodeType.FOREACH;
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
    }
}