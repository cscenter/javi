package view;

import java.util.ArrayList;
import java.util.UUID;

public final class JaviBlockSchemeBlock
{
    private String uuid;
    private ArrayList<ArrayList<JaviBlockSchemeBlock>> nestedStatements;
    private JaviBlockSchemeBlockType type;
    private String content;

    public JaviBlockSchemeBlock(JaviBlockSchemeBlockType type, String content, int branches)
    {
        this.uuid = UUID.randomUUID().toString();
        this.nestedStatements = new ArrayList<>();
        for (int i = 0; i < branches; ++i) {
            nestedStatements.add(new ArrayList<>());
        }

        this.type = type;
        this.content = content;
    }

    public ArrayList<ArrayList<JaviBlockSchemeBlock>> getNestedStatements() {
        return nestedStatements;
    }

    public String getContent()
    {
        return this.content;
    }

    public JaviBlockSchemeBlockType getType()
    {
        return this.type;
    }
}
