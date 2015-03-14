package org.antlr.java;

import java.beans.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class GraphicNode {
    public ArrayList<GraphicNode> children = new ArrayList<>();
    public int level = 0;
    public String value = "";
    public String name = "";

    public void addNode(GraphicNode n) {
        children.add(n);
    }

    public GraphicNode getChild(int i) {
        if (i >= children.size()) {
            return null;
        }
        GraphicNode c = children.get(i);
        return c;
    }

    public List<GraphicNode> getAllChild() {
        return children;
    }

    public abstract void paint();
}

class NodeMethod extends GraphicNode {

    public NodeMethod(String str) {
        level = 0;
        name = str;
    }

    @Override
    public void paint() {
        for (int i=0; i < level; i++) {
            System.out.print("-");
        }
        System.out.println(name);
    }
}

class NodeIf extends GraphicNode {
    public Statement elseStmt = null;

    public NodeIf(int level) {
        this.level = level;
    }

    public void setElseStmt(Statement elseS) {
        this.elseStmt =  elseS;
    }

    @Override
    public void paint() {
        for (int i=0; i < level; i++) {
            System.out.print("-");
        }
        System.out.println("IF");
    }
}

class NodeFor extends GraphicNode {

    public NodeFor(int level) {
        this.level = level;
    }

    @Override
    public void paint() {
        for (int i=0; i < level; i++) {
            System.out.print("-");
        }
        System.out.println("FOR");
    }
}

class NodeForeach extends GraphicNode {

    public NodeForeach(int level) {
        this.level = level;
    }

    @Override
    public void paint() {
        for (int i=0; i < level; i++) {
            System.out.print("-");
        }
        System.out.println("FOREACH");
    }
}

class NodeWhile extends GraphicNode {

    public NodeWhile(int level) {
        this.level = level;
    }

    @Override
    public void paint() {
        for (int i=0; i < level; i++) {
            System.out.print("-");
        }
        System.out.println("WHILE");
    }
}
