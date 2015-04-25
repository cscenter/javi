package new_model;

import com.github.antlrjavaparser.api.body.VariableDeclarator;
import com.github.antlrjavaparser.api.expr.VariableDeclarationExpr;

import java.util.stream.Collectors;

public class DeclarationNode extends Node {
    private String exp;

    public DeclarationNode(VariableDeclarationExpr node) {
        super(node);
        String vars = node.getVars().stream().map(VariableDeclarator::toString).collect(Collectors.joining(" "));
        exp = node.getType().toString() + " " + vars;
    }

    public String getExp()
    {
        return exp;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(exp);
        builder.append("\n");
        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.DECLARATION;
    }

    @Override
    public void attachInner(Node node) {

    }
}
