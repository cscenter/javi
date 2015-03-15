package model;

public class JaviNodeFor extends JaviNode
{
    public JaviNodeFor(int level) {
        this.level = level;
    }

    @Override
    public String paint() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < level; i++) {
            result.append("-");
        }
        result.append("FOR\n");

        return result.toString();
    }
}
