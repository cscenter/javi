package model;

import com.github.antlrjavaparser.api.expr.UnaryExpr;

import java.util.HashMap;
import java.util.Map;

public class UnaryNode extends Node {
    private String exp;
    private static Map<String, String> operators = new HashMap<String, String>();
    static {
        operators.put("preIncrement", "++");
        operators.put("posIncrement", "++");
        operators.put("preDecrement", "--");
        operators.put("posDecrement", "--");
        operators.put("positive", "+");
        operators.put("negative", "-");
        operators.put("not", "!");
        operators.put("inverse", "~");
    }

    public UnaryNode(UnaryExpr node) {
        super(node);
        if (node.getOperator().toString() == "posIncrement" || node.getOperator().toString() == "posDecrement") {
            this.exp = node.getExpr().toString() + operators.get(node.getOperator().toString());
        } else {
            this.exp = operators.get(node.getOperator().toString()) + node.getExpr().toString();
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
