package model;

public class JaviNodeMethod extends JaviNode
{
    public JaviNodeMethod(String str) {
        level = 0;
        name = str;
    }

    @Override
    public String paint() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < level; i++) {
            result.append("-");
        }
        return result.append(name).append("\n").toString();
    }
}
