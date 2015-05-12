package model;

import com.github.antlrjavaparser.api.expr.AssignExpr;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;

public class AssignNode extends Node {
    private String exp;
    static Map<String, String> operators = new HashMap<String, String>();
    static {
        operators.put("assign", "=");
        operators.put("plus", "+=");
        operators.put("minus", "-=");
        operators.put("slash", "\\=");
        operators.put("star", "*=");
        operators.put("or", "||=");
        operators.put("and", "&=");
        operators.put("xor", "^=");
        operators.put("rem", "%=");
        operators.put("lShift", "<<=");
        operators.put("rSignedShift", ">>=");
        operators.put("rUnsignedShift", ">>>=");
    }

    public AssignNode(AssignExpr node) {
        super(node.getTarget());
        this.exp = node.getTarget().toString() + " " + operators.get(node.getOperator())
                + " " + node.getValue().toString();
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