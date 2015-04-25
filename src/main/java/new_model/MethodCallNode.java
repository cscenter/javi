package new_model;

import com.github.antlrjavaparser.api.expr.Expression;
import com.github.antlrjavaparser.api.expr.MethodCallExpr;
import com.github.antlrjavaparser.api.type.Type;

import java.util.List;
import java.util.stream.Collectors;

public class MethodCallNode extends Node {
    private String method;

    public MethodCallNode(MethodCallExpr node) {
        super(node);

        List<Type> tArgs = node.getTypeArgs();
        List<Expression> arg = node.getArgs();

        String type = "";
        String args = "";

        if (tArgs != null) {
            type = tArgs.stream().map(Type::toString).collect(Collectors.joining(" "));
        }
        if (arg != null) {
            args = arg.stream().map(Expression::toString).collect(Collectors.joining(" "));
        }

        this.method = node.getScope().toString() + type + "." + node.getName() + "(" + args + ")";
    }

    public String getMethod()
    {
        return method;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(method);
        builder.append("\n");
        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.METHOD;
    }

    @Override
    public void attachInner(Node node) {

    }
}
