package new_model;

import com.github.antlrjavaparser.api.expr.UnaryExpr;

public class UnaryNode extends Node {
    private String exp;

    public UnaryNode(UnaryExpr node) {
        super(node);
        UnaryExpr.Operator op = node.getOperator();
        String operator = "";
        if (op.toString() == "preIncrement") {
            operator = "++";
            this.exp = operator + node.getExpr().toString();
        } else if (op.toString() == "posIncrement") {
            operator = "++";
            this.exp = node.getExpr().toString() + operator;
        } else if (op.toString() == "posDecrement") {
            operator = "--";
            this.exp = node.getExpr().toString() + operator;
        } else if (op.toString() == "preDecrement") {
            operator = "--";
            this.exp = operator + node.getExpr().toString();
        }
    }

    public String getExp()
    {
        return exp;
    }

    @Override
    public NodeType getType() {
        return NodeType.UNARY;
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
