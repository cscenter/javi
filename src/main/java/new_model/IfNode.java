package new_model;

import com.github.antlrjavaparser.api.stmt.IfStmt;

public class IfNode extends Node {
    private Node yes;
    private Node no;
    private String condition;

    public IfNode(IfStmt node) {
        super(node);
        this.condition = node.getCondition().toString();
    }

    public Node getYes() {
        return yes;
    }

    public void setYes(Node yes) {
        this.yes = yes;
    }

    public Node getNo() {
        return no;
    }

    public void setNo(Node no) {
        this.no = no;
    }

    public String getCondition()
    {
        return condition;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("if (").append(condition).append(") {\n");
        if (yes != null) {
            Node tmp = yes;
            while (tmp != null) {
                builder.append(tmp);
                tmp = tmp.next;
            }
        }

        if (no != null) {
            for (int i = 0; i < level; ++i) {
                builder.append("--");
            }
            builder.append("} else {\n");
            Node tmp = no;
            while (tmp != null) {
                builder.append(tmp);
                tmp = tmp.next;
            }
        }
        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append("}\n");

        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.IF;
    }

    @Override
    public void attachInner(Node node) {
        yes = node; // we will process NO node in other place
        node.level = this.level + 1;
    }
}
