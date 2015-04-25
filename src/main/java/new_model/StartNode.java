package new_model;

public class StartNode extends Node {
    public StartNode() {
        super(0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
    }

    @Override
    public String toString() {
        return "StartNode\n";
    }

    @Override
    public NodeType getType() {
        return NodeType.START;
    }

    @Override
    public void attachInner(Node node) {
        next = node;
        node.level = this.level + 1;
    }
}
