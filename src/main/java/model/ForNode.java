package model;

import com.github.antlrjavaparser.api.expr.Expression;
import com.github.antlrjavaparser.api.stmt.ForStmt;

import java.util.List;
import java.util.stream.Collectors;

public class ForNode extends Node {
    private Node nestedFirst;
    private String condition;

    public ForNode(ForStmt node) {
        super(node);
        List<Expression> init = node.getInit();
        List<Expression> update = node.getUpdate();
        Expression compare = node.getCompare();
        String inits = "";
        String updates = "";
        String compares = "";
        if (init != null) {
            inits = init.stream().map(Expression::toString).collect(Collectors.joining(" "));
        }
        if (update != null) {
            updates = update.stream().map(Expression::toString).collect(Collectors.joining(" "));
        }
        if (compare != null) {
            compares = compare.toString();
        }
        condition = inits + " ; " + compares + " ; " + updates;
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

        builder.append("for (").append(condition).append(") {\n");
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
        return NodeType.FOR;
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
    }
}