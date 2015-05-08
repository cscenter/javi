package model;

import com.github.antlrjavaparser.api.expr.AssignExpr;

public class AssignNode extends Node {
    private String exp;

    public AssignNode(AssignExpr node) {
        super(node.getTarget());
        AssignExpr.Operator op = node.getOperator();
        String operator = "";
        if (op.toString() == "assign") {
            operator = " = ";
        } else if (op.toString() == "plus") {
            operator = "+=";
        } else if (op.toString() == "minus") {
            operator = "-=";
        }

        this.exp = node.getTarget().toString() + " " + operator +
                " " + node.getValue().toString();
    }

    public String getExp()
    {
        return exp;
    }

    @Override
    public NodeType getType() {
        return NodeType.ASSIGN;
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