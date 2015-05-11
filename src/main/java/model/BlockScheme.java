package model;

// Block scheme consists of one lonely node.
public class BlockScheme {
    //private com.github.antlrjavaparser.api.Node node;
    private StartNode start = new StartNode();

    public StartNode getStart() {
        return start;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Node tmp = start;
        while (tmp != null) {
            sb.append(tmp);
            tmp = tmp.next;
        }
        return sb.toString();
    }
}