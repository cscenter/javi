package model;

import java.beans.Statement;

public class JaviNodeIf extends JaviNode
{
    public Statement elseStmt = null;

    public JaviNodeIf(int level) {
        this.level = level;
    }

    public void setElseStmt(Statement elseS) {
        this.elseStmt =  elseS;
    }

    @Override
    public String paint() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < level; i++) {
            result.append("-");
        }
        result.append("IF\n");

        return result.toString();
    }
}
