package model;

import com.github.antlrjavaparser.api.expr.BinaryExpr;

public class BinaryNode extends Node {
    private String exp;
    Map<String, String> operators = new HashMap<>();

    public BinaryNode(BinaryExpr node) {
        super(node);
        operators.put("equals", "==");
        operators.put("greater", ">");
        operators.put("less", "<");
        operators.put("greaterEquals", ">=");
        operators.put("lessEquals", "<=");
        operators.put("notEquals", "!=");
        operators.put("or", "||");
        operators.put("and", "&&");
        operators.put("xor", "^");
        operators.put("lShift", "<<");
        operators.put("rSignedShift", ">>");
        operators.put("rUnsignedShift", ">>>");
        operators.put("plus", ""); //?
        operators.put("minus", ""); //?
        operators.put("times", ""); //?
        operators.put("divide", "");
        operators.put("remainder", "%");
        operators.put("binOr", "");
        operators.put("binAnd", "");

        exp = node.getLeft().toString() + operators.get(node.getOperator()) + node.getRight().toString();
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