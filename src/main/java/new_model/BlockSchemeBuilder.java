package new_model;

import com.github.antlrjavaparser.api.Comment;
import com.github.antlrjavaparser.api.body.CatchParameter;
import com.github.antlrjavaparser.api.body.Resource;
import com.github.antlrjavaparser.api.expr.AssignExpr;
import com.github.antlrjavaparser.api.expr.LambdaExpr;
import com.github.antlrjavaparser.api.expr.MethodCallExpr;
import com.github.antlrjavaparser.api.expr.MethodReferenceExpr;
import com.github.antlrjavaparser.api.expr.VariableDeclarationExpr;
import com.github.antlrjavaparser.api.stmt.BreakStmt;
import com.github.antlrjavaparser.api.stmt.CatchClause;
import com.github.antlrjavaparser.api.stmt.ContinueStmt;
import com.github.antlrjavaparser.api.stmt.DoStmt;
import com.github.antlrjavaparser.api.stmt.ForStmt;
import com.github.antlrjavaparser.api.stmt.ForeachStmt;
import com.github.antlrjavaparser.api.stmt.IfStmt;
import com.github.antlrjavaparser.api.stmt.ReturnStmt;
import com.github.antlrjavaparser.api.stmt.Statement;
import com.github.antlrjavaparser.api.stmt.TryStmt;
import com.github.antlrjavaparser.api.stmt.WhileStmt;
import com.github.antlrjavaparser.api.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Stack;


public class BlockSchemeBuilder extends VoidVisitorAdapter {
    BlockScheme scheme;
    Stack<NodePosition> innerNodes = new Stack<>();
    Node currentNode;
    boolean processingElseBranch = false;

    public BlockSchemeBuilder(BlockScheme blockScheme) {
        scheme = blockScheme;
        currentNode = null;
        innerNodes.add(new NodePosition(0, Integer.MAX_VALUE, scheme.getStart()));
    }

    // is node "complex"?
    private boolean isBranchingNode(Node node) {
        if (node.getType() == NodeType.IF
                || node.getType() == NodeType.FOR
                || node.getType() == NodeType.FOREACH
                || node.getType() == NodeType.WHILE
                || node.getType() == NodeType.DOWHILE
                || node.getType() == NodeType.TRY
                || node.getType() == NodeType.CATCH) {
            return true;
        }
        return false;
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
        NodePosition currentNodePosition = new NodePosition(antlrNode.getBeginLine(), antlrNode.getEndLine(), node);
        NodePosition prevNodePosition = innerNodes.peek();

        // special case else branch
        if (processingElseBranch) {
            processElseNode(antlrNode);
            return;
        }

        // 1'st
        if (currentNode == null) {
            assert prevNodePosition.contains(currentNodePosition);

            prevNodePosition.node.attachInner(node);
            if (isBranchingNode(node)) {
                innerNodes.add(currentNodePosition);
                currentNode = null;
            } else {
                currentNode = node;
            }
            return;
        }

        //2'nd
        assert currentNode != null;
        if (prevNodePosition.contains(currentNodePosition)) {
            currentNode.setNext(node);
            node.level = currentNode.level;
            if (isBranchingNode(node)) {
                innerNodes.add(currentNodePosition);
                currentNode = null;
            } else {
                currentNode = node;
            }
            return;
        }

        //3'rd
        if (!prevNodePosition.contains(currentNodePosition)) {
            NodePosition lastNotContaining = innerNodes.peek();
            while (!innerNodes.peek().contains(currentNodePosition)) {
                lastNotContaining = innerNodes.pop();
            }
            lastNotContaining.node.setNext(node);
            node.level = lastNotContaining.node.level;

            if (isBranchingNode(node)) {
                innerNodes.add(currentNodePosition);
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
        IfNode node = (IfNode) innerNodes.peek().node; // Точно в этом уверены

        Node newNode = createMyNodeFromAntlrNode(antlrNode);
        NodePosition currentNodePosition = new NodePosition(antlrNode.getBeginLine(), antlrNode.getEndLine(), newNode);

        node.setNo(newNode);
        newNode.level = node.level + 1;
        if (isBranchingNode(newNode)) {
            innerNodes.add(currentNodePosition);
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
            return new IfNode();
        } else if (antlrNode instanceof ForStmt) {
            return new ForNode();
        } else if (antlrNode instanceof ForeachStmt) {
            return new ForEachNode();
        } else if (antlrNode instanceof WhileStmt) {
            WhileStmt condition = (WhileStmt) antlrNode;
            return new WhileNode(condition.getCondition());
        } else if (antlrNode instanceof DoStmt) {
            DoStmt condition = (DoStmt) antlrNode;
            return new DoWhileNode(condition.getCondition());
        } else if (antlrNode instanceof AssignExpr) {
            AssignExpr expr = (AssignExpr) antlrNode;
            return new AssignNode(expr.getTarget().toString(), expr.getValue().toString());
        } else if (antlrNode instanceof VariableDeclarationExpr) {
            VariableDeclarationExpr expr = (VariableDeclarationExpr) antlrNode;
            return new DeclarationNode(expr.getType(), expr.getVars(), expr.getAnnotations());
        } else if (antlrNode instanceof MethodCallExpr) {
            MethodCallExpr method = (MethodCallExpr) antlrNode;
            return new MethodCallNode(method);
        } else if (antlrNode instanceof ReturnStmt) {
            ReturnStmt expr = (ReturnStmt) antlrNode;
            return new ReturnNode(expr.getExpr());
        } else if (antlrNode instanceof BreakStmt) {
            return new BreakNode();
        } else if (antlrNode instanceof ContinueStmt) {
            return new ContinueNode();
        } else if (antlrNode instanceof CatchClause) {
            CatchClause clause = (CatchClause) antlrNode;
            return new CatchNode(clause);
        } else if (antlrNode instanceof TryStmt) {
            return new TryNode();
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
    public void visit(VariableDeclarationExpr declarationExpr, Object o) {
        processNode(declarationExpr);
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
    }

    @Override
    public void visit(TryStmt tryStmt, Object o) {
        processNode(tryStmt);
        Statement loopBody = tryStmt.getTryBlock();
        loopBody.accept(this, o);
        List<CatchClause> catchList = tryStmt.getCatchs();
        for (CatchClause c : catchList) {
            c.accept(this, o);
        }
    }

    @Override
    public void visit(CatchClause catchClause, Object o) {
        processNode(catchClause);
        Statement loopBody = catchClause.getCatchBlock();
        loopBody.accept(this, o);
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
        if (innerNodes.size() > 1) {
            while (innerNodes.size() != 2) {
                innerNodes.pop();
            }
            NodePosition top = innerNodes.pop();
            top.node.setNext(new EndNode());
        } else {
            assert currentNode != null;
            currentNode.setNext(new EndNode());
        }
    }
}
