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

enum NodeType {
    START,
    END,
    FOR,
    IF,
    DECLARATION,
    ASSIGN
}

abstract class Node {
    Node next;
    int level;

    // Get the node type
    public abstract NodeType getType();

    // Attach inner node. For: nestedFirst = node, if: yes = node.
    public abstract void attachInner(Node node);
}

class StartNode extends Node {
    @Override
    public String toString() {
        return "StartNode\n";
    }

    @Override
    public NodeType getType() {
        return NodeType.START;
    }

    @Override
    public void attachInner(Node node) {
        next = node;
        node.level = this.level + 1;
    }
}


class EndNode extends Node {
    @Override
    public String toString() {
        return "EndNode";
    }

    @Override
    public NodeType getType() {
        return NodeType.END;
    }

    @Override
    public void attachInner(Node node) {
    }
}

class IfNode extends Node {
    public Node yes;
    public Node no;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("if {\n");
        if (yes != null) {
            builder.append(yes);
        }

        if (no != null) {
            for (int i = 0; i < level; ++i)
                builder.append("--");
            builder.append("} else {\n");
            builder.append(no);
        }
        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append("}\n");

        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.IF;
    }

    @Override
    public void attachInner(Node node) {
        yes = node; // we will process NO node in other place
        node.level = this.level + 1;
    }
}

class ForNode extends Node {
    public Node nestedFirst;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("for {\n");
        while (tmp != null) {
            builder.append(tmp.toString());
            tmp = tmp.next;
        }

        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append("}\n");
        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.FOR;
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
    }
}

class AssignNode extends Node {

    private final String left;
    private final String right;

    public AssignNode(String left, String right) {

        this.left = left;
        this.right = right;
    }


    @Override
    public NodeType getType() {
        return NodeType.ASSIGN;
    }

    @Override
    public void attachInner(Node node) {
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append(left).append(" op ").append(right).append("\n");
        return builder.toString();
    }
}

class DeclarationNode extends Node {
    private Type type;
    private List<VariableDeclarator> vars;
    private List<AnnotationExpr> annotations;

    public DeclarationNode(Type type, List<VariableDeclarator> vars, List<AnnotationExpr> annotations) {

        this.type = type;
        this.vars = vars;
        this.annotations = annotations;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(type.toString()).append(" ");
        for (VariableDeclarator var : vars) {
            builder.append(var).append(" ");
        }
        builder.append("\n");
        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.DECLARATION;
    }

    @Override
    public void attachInner(Node node) {
    }
}


// Block scheme consists of one lonely node.
class BlockScheme {
    public StartNode start = new StartNode();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Node tmp = start;
        while (tmp != null) {
            sb.append(tmp);
            tmp = tmp.next;
        }
        return sb.toString();
    }
}

// This class incapsulates the node, and its line numbers.
// We need it to determine whether one node contains the other or not
class NodePosition {
    int start;
    int end;
    Node node;

    public NodePosition(int start, int end, Node node) {
        this.start = start;
        this.end = end;
        this.node = node;
    }

    public boolean contains(NodePosition other) {
        return other.start > this.start && other.end < this.end;
    }
}

class BlockSchemeBuilder extends VoidVisitorAdapter {

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

public class ModelTest {
    public static void main(String[] args) throws IOException {
        File file = new File("examples/FizzBuzz.java");
        // parse file
        CompilationUnit fileParsed = JavaParser.parse(file);

        List<TypeDeclaration> classes = fileParsed.getTypes();
        List<BodyDeclaration> members = classes.get(0).getMembers();

        List<MethodDeclaration> methods = new ArrayList<>();
        for (int i = 0; i < members.size(); ++i)
        {
            if (members.get(i) instanceof MethodDeclaration)
            {
                methods.add((MethodDeclaration) members.get(i));
            }
        }

        // find main
        MethodDeclaration main_ = null;
        for (int i = 0; i < methods.size(); ++i)
        {
            if (methods.get(i).getName().equals("main")) {
                main_ = methods.get(i);
                break;
            }
        }
        assert main_ != null;

        // accept visitor in main.
        BlockScheme blockScheme = new BlockScheme();
        BlockSchemeBuilder visitor = new BlockSchemeBuilder(blockScheme);
        main_.accept(visitor, null);
        visitor.finish();

        System.out.println(blockScheme);
    }
}
