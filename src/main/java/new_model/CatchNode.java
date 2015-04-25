package new_model;

import com.github.antlrjavaparser.api.stmt.CatchClause;

public class CatchNode extends Node {
    private Node nestedFirst;
    private String except;

    public CatchNode(CatchClause node) {
        super(node);
        this.except = node.getExcept().toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("} catch (").append(except).append(") {\n");
        while (tmp != null) {
            builder.append(tmp.toString());
            tmp = tmp.next;
        }

        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.CATCH;
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
    }
}
