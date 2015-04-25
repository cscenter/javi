package new_model;

import com.github.antlrjavaparser.api.stmt.BlockStmt;

public class FinallyNode extends Node {
    private Node nestedFirst;
    private String expFinally;

    protected FinallyNode(BlockStmt node) {
        super(node);
        this.expFinally = "finally:";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("} finally {\n");
        while (tmp != null) {
            builder.append(tmp.toString());
            tmp = tmp.next;
        }

        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append("} \n");
        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.FINALLY;
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
    }
}
