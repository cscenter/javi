package model;

import java.util.ArrayList;
import java.util.List;

public abstract class JaviNode
{
    public ArrayList<JaviNode> children = new ArrayList<>();
    public int level = 0;
    public String value = "";
    public String name = "";

    public void addNode(JaviNode n) {
        children.add(n);
    }

    public JaviNode getChild(int i) {
        if (i >= children.size()) {
            return null;
        }
        return children.get(i);
    }

    public List<JaviNode> getAllChild() {
        return children;
    }

    public abstract String paint();
}
