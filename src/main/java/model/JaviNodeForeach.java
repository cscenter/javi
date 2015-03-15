package model;

public class JaviNodeForeach extends JaviNode
{
    public JaviNodeForeach(int level) {
        this.level = level;
    }

    @Override
    public String paint() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < level; i++) {
            result.append("-");
        }
        result.append("FOREACH\n");

        return result.toString();
    }
}
