package new_model;

import com.github.antlrjavaparser.api.stmt.ReturnStmt;

public class ReturnNode extends Node{
    private String expression;

    public ReturnNode(ReturnStmt node) {
        super(node);
        this.expression = "return " + node.getExpr().toString();
    }

    public String getExpression()
    {
        return expression;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(expression);
        builder.append("\n");
        return  builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.RETURN;
    }

    @Override
    public void attachInner(Node node) {
    }
}
