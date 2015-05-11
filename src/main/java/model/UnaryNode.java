package model;

import com.github.antlrjavaparser.api.expr.UnaryExpr;

public class UnaryNode extends Node {
    private String exp;
    Map<String, String> operators = new HashMap<>();

    public UnaryNode(UnaryExpr node) {
        super(node);
        operators.put("preIncrement", "++");
        operators.put("posIncrement", "++");
        operators.put("preDecrement", "--");
        operators.put("posDecrement", "--");
        operators.put("positive", "+"); //?
        operators.put("negative", "-"); //?
        operators.put("not", "~"); //?
        operators.put("inverse", ""); //?

        if (node.getOperator().toString() == "preIncrement" || node.getOperator().toString() == "preDecrement") {
            this.exp = operators.get(node.getOperator()) + node.getExpr().toString();
//        } else if (node.getOperator().toString() == "posIncrement" || node.getOperator().toString() == "posDecrement") {
//            this.exp = node.getExpr().toString() + operators.get(node.getOperator());
//        }
        } else {
            this.exp = node.getExpr().toString() + operators.get(node.getOperator());
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
