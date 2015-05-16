package model;

import com.github.antlrjavaparser.api.Comment;
import com.github.antlrjavaparser.api.body.CatchParameter;
import com.github.antlrjavaparser.api.body.Resource;
import com.github.antlrjavaparser.api.expr.AssignExpr;
import com.github.antlrjavaparser.api.expr.BinaryExpr;
import com.github.antlrjavaparser.api.expr.LambdaExpr;
import com.github.antlrjavaparser.api.expr.MethodCallExpr;
import com.github.antlrjavaparser.api.expr.MethodReferenceExpr;
import com.github.antlrjavaparser.api.expr.UnaryExpr;
import com.github.antlrjavaparser.api.expr.VariableDeclarationExpr;
import com.github.antlrjavaparser.api.stmt.BreakStmt;
import com.github.antlrjavaparser.api.stmt.CatchClause;
import com.github.antlrjavaparser.api.stmt.ContinueStmt;
import com.github.antlrjavaparser.api.stmt.DoStmt;
import com.github.antlrjavaparser.api.stmt.ForStmt;
import com.github.antlrjavaparser.api.stmt.ForeachStmt;
import com.github.antlrjavaparser.api.stmt.IfStmt;
import com.github.antlrjavaparser.api.stmt.LabeledStmt;
import com.github.antlrjavaparser.api.stmt.ReturnStmt;
import com.github.antlrjavaparser.api.stmt.Statement;
import com.github.antlrjavaparser.api.stmt.SwitchEntryStmt;
import com.github.antlrjavaparser.api.stmt.SwitchStmt;
import com.github.antlrjavaparser.api.stmt.ThrowStmt;
import com.github.antlrjavaparser.api.stmt.TryStmt;
import com.github.antlrjavaparser.api.stmt.WhileStmt;
import com.github.antlrjavaparser.api.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


public class BlockSchemeBuilder extends VoidVisitorAdapter {
    private BlockScheme scheme;
    private Stack<Node> innerNodes = new Stack<>();
    private Stack<Node> breakableStack = new Stack<>();
    private Node currentNode;
    private boolean processingElseBranch = false;
    private ArrayList<ReturnNode> returnNodes = new ArrayList<>();
    private Map<String, LabelNode> labels = new HashMap<>();
    private Map<BreakNode, Node> breaks = new HashMap<>();

    public BlockSchemeBuilder(BlockScheme blockScheme) {
        scheme = blockScheme;
        currentNode = null;
        innerNodes.add(scheme.getStart());
    }

    // is node "complex"?
    private boolean isBranchingNode(Node node) {
        if (node.getType() == NodeType.IF
                || node.getType() == NodeType.FOR
                || node.getType() == NodeType.FOREACH
                || node.getType() == NodeType.WHILE
                || node.getType() == NodeType.DOWHILE
                || node.getType() == NodeType.CATCH
                || node.getType() == NodeType.TRY
                || node.getType() == NodeType.SWITCH
                || node.getType() == NodeType.CASE
                || node.getType() == NodeType.LABEL) {
            return true;
        }
        return false;
    }

    private boolean isBreakable(Node node) {
        return node.getType() == NodeType.FOR
                || node.getType() == NodeType.FOREACH
                || node.getType() == NodeType.WHILE
                || node.getType() == NodeType.SWITCH;
    }

    //EL: лучше сделать более короткие имена типов
    private void processNode(com.github.antlrjavaparser.api.Node antlrNode) {
        /*
        * 1) inside stack top complex node, and current node == null
        * 2) inside stack top complex node, and current node != null
        * 3) outside of stack top complex node, and current node != null
        * 4) invalid situation
        * */

        assert !innerNodes.empty();

        Node node = createMyNodeFromAntlrNode(antlrNode);
        if (isBreakable(node)) {
            breakableStack.push(node);
        }
        if (!breakableStack.empty()) {
            while (!breakableStack.peek().contains(node)) {
                breakableStack.pop();
                if (breakableStack.empty()) {
                    break;
                }
            }
        }

        Node prevNode = innerNodes.peek();

        // special case else branch
        if (processingElseBranch) {
            processElseNode(antlrNode);
            return;
        }

        // 1'st
        if (currentNode == null) {
            if (prevNode.contains(node)) {
                prevNode.attachInner(node);
                if (isBranchingNode(node)) {
                    innerNodes.add(node);
                    currentNode = null;
                } else {
                    currentNode = node;
                }
                return;
            }
        }

        //2'nd
        if (prevNode.contains(node)) {
            currentNode.setNext(node);
            node.level = currentNode.level;

            if (node.getType() == NodeType.CONTINUE) {
                ContinueNode continueNode = (ContinueNode) node;
                if (continueNode.getLabel() == null) {
                    node.setNext(breakableStack.peek());
                } else {
                    node.setNext(labels.get(continueNode.getLabel()));
                }
            }
            if (node.getType() == NodeType.BREAK) {
                BreakNode breakNode = (BreakNode) node;
                if (breakNode.getLabel() == null) {
                    breaks.put(breakNode, breakableStack.peek());
                } else {
                    breaks.put(breakNode, labels.get(breakNode.getLabel()));
                }
            }

            if (isBranchingNode(node)) {
                innerNodes.add(node);
                currentNode = null;
            } else {
                currentNode = node;
            }
            return;
        }

        //3'rd
        if (!prevNode.contains(node)) {
            Node lastNotContaining = innerNodes.peek();
            while (!innerNodes.peek().contains(node)) {
                lastNotContaining = innerNodes.pop();
            }
            lastNotContaining.setNext(node);
            node.level = lastNotContaining.level;

            if (isBranchingNode(node)) {
                innerNodes.add(node);
                currentNode = null;
            }
            else {
                currentNode = node;
            }
        }
    }

    /*
     Special case node processing.
    * */
    public void processElseNode(com.github.antlrjavaparser.api.Node antlrNode) {
        IfNode node = (IfNode) innerNodes.peek(); // Точно в этом уверены

        Node newNode = createMyNodeFromAntlrNode(antlrNode);

        node.setNo(newNode);
        newNode.level = node.level + 1;
        if (isBranchingNode(newNode)) {
            innerNodes.add(newNode);
            currentNode = null;
        }
        else {
            currentNode = newNode;
        }

        processingElseBranch = false;
    }

    /* Create our model node from ANTLR node
    * Name collision with ANTLR node
    * */
    private Node createMyNodeFromAntlrNode(com.github.antlrjavaparser.api.Node antlrNode) {
        if (antlrNode instanceof IfStmt) {
            return new IfNode((IfStmt)antlrNode);
        } else if (antlrNode instanceof ForStmt) {
            return new ForNode((ForStmt)antlrNode);
        } else if (antlrNode instanceof ForeachStmt) {
            return new ForEachNode((ForeachStmt)antlrNode);
        } else if (antlrNode instanceof WhileStmt) {
            return new WhileNode((WhileStmt)antlrNode);
        } else if (antlrNode instanceof AssignExpr) {
            return new AssignNode((AssignExpr) antlrNode);
        } else if (antlrNode instanceof UnaryExpr) {
            return new UnaryNode((UnaryExpr) antlrNode);
        } else if (antlrNode instanceof BinaryExpr){
            return new BinaryNode((BinaryExpr) antlrNode);
        } else if (antlrNode instanceof VariableDeclarationExpr) {
            return new DeclarationNode((VariableDeclarationExpr) antlrNode);
        } else if (antlrNode instanceof MethodCallExpr) {
            MethodCallExpr method = (MethodCallExpr) antlrNode;
            return new MethodCallNode(method);
        } else if (antlrNode instanceof ReturnStmt) {
            ReturnNode returnNode = new ReturnNode((ReturnStmt) antlrNode);
            returnNodes.add(returnNode);
            return returnNode;
        } else if (antlrNode instanceof BreakStmt) {
            return new BreakNode((BreakStmt)antlrNode);
        } else if (antlrNode instanceof ContinueStmt) {
            return new ContinueNode((ContinueStmt)antlrNode);
        } else if (antlrNode instanceof CatchClause) {
            return new CatchNode((CatchClause) antlrNode);
        } else if (antlrNode instanceof TryStmt) {
            return new TryNode((TryStmt)antlrNode);
        } else if (antlrNode instanceof SwitchEntryStmt) {
            return new CaseNode((SwitchEntryStmt)antlrNode);
        } else if (antlrNode instanceof SwitchStmt) {
            return new SwitchNode((SwitchStmt) antlrNode );
        } else if (antlrNode instanceof ThrowStmt) {
            return new ThrowNode((ThrowStmt) antlrNode);
        } else if (antlrNode instanceof LabeledStmt) {
            LabelNode n = new LabelNode((LabeledStmt) antlrNode);
            labels.put(n.getLabel(), n);
            return n;
        }
        assert false;
        return null;
    }

    @Override
    public void visit(IfStmt ifStmt, Object o) {
        processNode(ifStmt);

//        Expression condition = ifStmt.getCondition();
        Statement then = ifStmt.getThenStmt();
        Statement elseStmt = ifStmt.getElseStmt();

        then.accept(this, o);
        if (elseStmt != null) {
            processingElseBranch = true;
            elseStmt.accept(this, o);
            processingElseBranch = false;
        }

    }

    @Override
    public void visit(ForStmt forStmt, Object o) {
        processNode(forStmt);
        Statement loopBody = forStmt.getBody();
        loopBody.accept(this, o);
    }

    @Override
    public void visit(ForeachStmt foreachStmt, Object o) {
        processNode(foreachStmt);
        Statement loopBody = foreachStmt.getBody();
        loopBody.accept(this, o);
    }

    @Override
    public void visit(WhileStmt whileStmt, Object o) {
        processNode(whileStmt);
        Statement loopBody = whileStmt.getBody();
        loopBody.accept(this, o);
    }

    @Override
    public void visit(DoStmt doStmt, Object o) {
        processNode(doStmt);
        Statement loopBody = doStmt.getBody();
        loopBody.accept(this, o);
    }

    @Override
    public void visit(AssignExpr assignExpr, Object o) {
        processNode(assignExpr);
    }

    @Override
    public void visit(UnaryExpr unaryExpr, Object o){
        processNode(unaryExpr);
    }

    @Override
    public void visit(BinaryExpr binaryExpr, Object o){
        processNode(binaryExpr);
    }

    @Override
    public void visit(VariableDeclarationExpr declarationExpr, Object o) {
        processNode(declarationExpr);
    }

    @Override
    public void visit(LabeledStmt labeledStmt, Object o) {
        processNode(labeledStmt);
        Statement loopBody = labeledStmt.getStmt();
        loopBody.accept(this, o);
    }

    @Override
    public void visit(MethodCallExpr methodCallExpr, Object o) {
        processNode(methodCallExpr);
    }

    @Override
    public void visit(ReturnStmt returnStmt, Object o) {
        processNode(returnStmt);
    }

    @Override
    public void visit(BreakStmt breakStmt, Object o) {
        processNode(breakStmt);
    }

    @Override
    public void visit(ContinueStmt continueStmt, Object o) {
        processNode(continueStmt);
        if (continueStmt.getId() == null) {

        }
    }

    @Override
    public void visit(TryStmt tryStmt, Object o) {
        processNode(tryStmt);
        Statement loopBody = tryStmt.getTryBlock();
        loopBody.accept(this, o);
        List<CatchClause> catchList = tryStmt.getCatchs();
        if (catchList != null) {
            for (CatchClause c : catchList) {
                c.accept(this, o);
            }
        }
        if (tryStmt.getFinallyBlock() != null) {
            unwindStack(NodeType.TRY);
            assert innerNodes.peek().getType() == NodeType.TRY;
            innerNodes.push(((TryNode)innerNodes.peek()).getFinallyBlock());
            currentNode = null;
            tryStmt.getFinallyBlock().accept(this, o);
            unwindStack(NodeType.TRY);
        }
    }

    private void unwindStack(NodeType nodeType) {
        while (innerNodes.peek().getType() != nodeType) {
            innerNodes.pop();
        }
    }

    @Override
    public void visit(CatchClause catchClause, Object o) {
        unwindStack(NodeType.TRY);
        CatchNode catchNode = new CatchNode(catchClause);
        innerNodes.peek().attachInner(catchNode);
        innerNodes.push(catchNode);
        currentNode = null;

        Statement loopBody = catchClause.getCatchBlock();
        loopBody.accept(this, o);
        unwindStack(NodeType.TRY);
    }

    @Override
    public void visit(SwitchStmt switchStmt, Object o) {
        processNode(switchStmt);
        List<SwitchEntryStmt> switchEntryStmts = switchStmt.getEntries();
        if (switchEntryStmts != null) {
            for (SwitchEntryStmt c : switchEntryStmts) {
                c.accept(this, o);
            }
        }
    }

    @Override
    public void visit(SwitchEntryStmt switchEntryStmt, Object o) {
        processNode(switchEntryStmt);
        List<Statement> statements = switchEntryStmt.getStmts();
        if (statements != null) {
            for (Statement s : statements) {
                s.accept(this, o);
            }
        }
    }

    @Override
    public void visit(ThrowStmt throwStmt, Object o) {
        processNode(throwStmt);
    }

    @Override
    public void visit(Comment comment, Object o) {

    }

    @Override
    public void visit(CatchParameter catchParameter, Object o) {

    }

    @Override
    public void visit(Resource resource, Object o) {

    }

    @Override
    public void visit(LambdaExpr lambdaExpr, Object o) {

    }

    @Override
    public void visit(MethodReferenceExpr methodReferenceExpr, Object o) {
    }

    /* Explicitly call this to attach EndNode to our model */
    public void finish() {
        EndNode endNode = new EndNode();
        if (innerNodes.size() > 1) {
            while (innerNodes.size() != 2) {
                innerNodes.pop();
            }
            Node top = innerNodes.pop();
            top.setNext(endNode);
        } else {
            if (currentNode != null) {
                currentNode.setNext(endNode);
            }
        }

        for (ReturnNode returnNode : returnNodes) {
            returnNode.setNext(endNode);
        }

        Set<BreakNode> keys = breaks.keySet();
        for (BreakNode key : keys) {
            Node n = breaks.get(key);
            key.setNext(n.getNext());
        }

    }
}