package model;

import com.github.antlrjavaparser.api.stmt.SwitchStmt;

import java.util.ArrayList;

public class SwitchNode extends Node {
    ArrayList<CaseNode> entries = new ArrayList<>();
    private String selector;
    public SwitchNode(SwitchStmt node) {
        super(node);
        this.selector = "switch" + node.getSelector().toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(selector).append(" { \n");

        CaseNode tmp = entries.get(0);
        while (tmp != null) {
            builder.append(tmp);
            tmp = (CaseNode)tmp.next;
        }

        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append("}\n");
        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.SWITCH;
    }

    @Override
    public void attachInner(Node node) {
        assert node instanceof CaseNode;
        node.level = this.level + 1;
        entries.add((CaseNode)node);
    }

    public  String getSelector()
    {
        return selector;
    }

    public ArrayList<CaseNode> getEntries()
    {
        return entries;
    }
}
