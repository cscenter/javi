package model;

public class JaviNodeWhile extends JaviNode
{
    public JaviNodeWhile(int level) {
        this.level = level;
    }

    @Override
    public String paint() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < level; i++) {
            result.append("-");
        }
        result.append("WHILE\n");

        return result.toString();
    }
}
