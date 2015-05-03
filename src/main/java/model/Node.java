package model;

//EL: не все классы в иерархии наследования имеют nestedNode => в базовом класса attachInner не нужен
public abstract class Node {
    protected Node next;
    protected int level;
    protected int startLine;
    protected int endLine;
    protected int startColumn;
    protected int endColumn;

    protected Node(com.github.antlrjavaparser.api.Node node) {
        startLine = node.getBeginLine();
        endLine = node.getEndLine();
        startColumn = node.getBeginColumn();
        endColumn = node.getEndColumn();
    }

    protected Node(int startLine, int endLine, int startColumn, int endColumn) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
    }

       public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public void setStartLine(int pos) {
        this.startLine = pos;
    }

    public void setEndLine(int pos) {
        this.endLine = pos;
    }

    public void setStartColumn(int pos) {
        this.startColumn = pos;
    }

    public void setEndColumn(int pos) {
        endColumn = pos;
    }

    public boolean contains(Node n) {
//        if (this.getStartLine() < n.getStartLine() && this.getEndLine() >= n.getEndLine()) {
//            return true;
//        } else {
//            return this.getStartColumn() < n.getStartColumn() && this.getEndColumn() >= n.getEndColumn();
//        }
        if (getStartLine() == n.getStartLine()) {
            if (getEndLine() == n.getEndLine()) {
                return getStartColumn() <= n.getStartColumn() && getEndColumn() >= n.getEndColumn();
            } else {
                return getEndLine() > n.getEndLine();
            }
        } else {
            if (getEndLine() == n.getEndLine()) {
                return getStartLine() < n.getStartLine() && getEndColumn() >= n.getEndColumn();
            } else {
                return getStartLine() < n.getStartLine() && getEndLine() > n.getEndLine();
            }
        }
    }

    // Get the node type
    public abstract NodeType getType();

    // Attach inner node. For: nestedFirst = node, if: yes = node.
    public abstract void attachInner(Node node);

    public Node getNext() {
        return next;
    }

    public void setNext(Node node) {
        this.next = node;
    }
}
