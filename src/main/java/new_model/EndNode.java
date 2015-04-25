package new_model;

public class EndNode extends Node {
    public EndNode() {
        super(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public String toString() {
        return "EndNode";
    }

    @Override
    public NodeType getType() {
        return NodeType.END;
    }

    @Override
    public void attachInner(Node node) {

    }
}
