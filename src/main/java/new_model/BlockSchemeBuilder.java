package new_model;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.Comment;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.body.*;
import com.github.antlrjavaparser.api.expr.*;
import com.github.antlrjavaparser.api.stmt.Statement;
import com.github.antlrjavaparser.api.stmt.ForStmt;
import com.github.antlrjavaparser.api.stmt.IfStmt;
import com.github.antlrjavaparser.api.type.Type;
import com.github.antlrjavaparser.api.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        innerNodes.add(new NodePosition(0, Integer.MAX_VALUE, scheme.start));
    }

    // is node "complex"?
    private boolean isBranchingNode(Node node) {
        if (node.getType() == NodeType.IF || node.getType() == NodeType.FOR) {
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
            currentNode.next = node;
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
            lastNotContaining.node.next = node;
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

        node.no = newNode;
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
        } else if (antlrNode instanceof AssignExpr) {
            AssignExpr expr = (AssignExpr) antlrNode;
            return new AssignNode(expr.getTarget().toString(), expr.getValue().toString());
        } else if (antlrNode instanceof VariableDeclarationExpr) {
            VariableDeclarationExpr expr = (VariableDeclarationExpr) antlrNode;
            return new DeclarationNode(expr.getType(), expr.getVars(), expr.getAnnotations());
        }
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
    public void visit(AssignExpr assignExpr, Object o) {
        processNode(assignExpr);
    }

    @Override
    public void visit(VariableDeclarationExpr declarationExpr, Object o) {
        processNode(declarationExpr);
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
        if (currentNode != null) {
            currentNode.next = new EndNode();
        } else {
            innerNodes.peek().node.next = new EndNode();
        }
    }
}
