package new_model;

import com.github.antlrjavaparser.api.stmt.ContinueStmt;

public class ContinueNode extends Node {
    private String exp;
    public ContinueNode(ContinueStmt node) {
        super(node);
        String id = "";
        if (node.getId() != null) {
            id = node.getId();
        }
        this.exp = "continue" + " " + id;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(exp).append("\n");
        return  builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.CONTINUE;
    }

    @Override
    public void attachInner(Node node) {
    }
}
