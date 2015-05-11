package model;

import com.github.antlrjavaparser.api.stmt.SwitchEntryStmt;

public class CaseNode extends Node {
    private Node nestedFirst;
    private String strCase;
    public CaseNode(SwitchEntryStmt node) {
        super(node);
        if (node.getLabel() != null) {
            strCase = ("case " + node.getLabel().toString() + ": \n");
        } else {
            strCase = ("default: \n");
        }
    }

    @Override
    public NodeType getType() {
        return NodeType.CASE;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append(strCase);

        Node tmp = nestedFirst;
        while (tmp != null) {
            builder.append(tmp.toString());
            tmp = tmp.next;
        }

        return builder.toString();
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
    }
}