package model;

import com.github.antlrjavaparser.api.expr.BinaryExpr;

public class BinaryNode extends Node {
    private String exp;

    public BinaryNode(BinaryExpr node) {
        super(node);
        BinaryExpr.Operator op = node.getOperator();
        String operator = "";
        if (op.toString() == "equals") {
            operator = " == ";
        } else if (op.toString() == "greater") {
            operator = " > ";
        } else if (op.toString() == "less") {
            operator = " < ";
        } else if (op.toString() == "greaterEquals") {
            operator = " >= ";
        } else if (op.toString() == "lessEquals") {
            operator = " <= ";
        } else if (op.toString() == "notEquals") {
            operator = " != ";
        }

        exp = node.getLeft().toString() + operator + node.getRight().toString();
    }

    public String getExp()
    {
        return exp;
    }

    @Override
    public NodeType getType() {
        return NodeType.BINARY;
    }

    @Override
    public void attachInner(Node node) {

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append(exp).append("\n");
        return builder.toString();
    }
}
