package view;

import java.util.ArrayList;
import java.util.UUID;

public final class JaviBlockSchemeBlock
{
    private String uuid;
    private ArrayList<JaviBlockSchemeBlock> outgoingBlocks;
    private ArrayList<ArrayList<JaviBlockSchemeBlock>> nestedStatements;
    private JaviBlockSchemeBlockType type;
    private String content;

    public JaviBlockSchemeBlock()
    {
        this.uuid = UUID.randomUUID().toString();
        this.outgoingBlocks = new ArrayList<>();
        this.nestedStatements = new ArrayList<>();
        nestedStatements.add(new ArrayList<>());
        this.type = JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeMethod;
        this.content = "";
    }

    public JaviBlockSchemeBlock(JaviBlockSchemeBlockType type, String content)
    {
        this.uuid = UUID.randomUUID().toString();
        this.outgoingBlocks = new ArrayList<>();
        this.nestedStatements = new ArrayList<>();
        nestedStatements.add(new ArrayList<>());
        if (type.equals(JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeIfStatement)) {
            nestedStatements.add(new ArrayList<>());
        }
        this.type = type;
        this.content = content;
    }

    public ArrayList<ArrayList<JaviBlockSchemeBlock>> getNestedStatements() {
        return nestedStatements;
    }

    public final void addOutgoingBlock(JaviBlockSchemeBlock block)
    {
        this.outgoingBlocks.add(block);
    }

    public void setCorrectOutgoingBlocks() {
        for (ArrayList<JaviBlockSchemeBlock> statements : nestedStatements) {
            if (!statements.isEmpty()) {
                setCorrectOutgoingBlocksForSequentialStatements(statements);
            }
        }
    }

    private void setCorrectOutgoingBlocksForSequentialStatements(ArrayList<JaviBlockSchemeBlock> statements) {
        JaviBlockSchemeBlock baseBlock = statements.get(0);
        baseBlock.setCorrectOutgoingBlocks();
        for (int i = 1; i < statements.size(); i++) {
            JaviBlockSchemeBlock currentBlock = statements.get(i);
            currentBlock.setCorrectOutgoingBlocks();

            baseBlock.addOutgoingBlock(currentBlock);
            baseBlock = currentBlock;
        }
    }
}
