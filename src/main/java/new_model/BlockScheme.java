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


//EL: все поля должны быть приватными
//EL: не все классы в иерархии наследования имеют nestedNode => в базовом класса attachInner не нужен
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


//EL: зачем нам два поля left и right?
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

//EL: модель должна быть устроена так, чтобы классу, который выводит на экран, было удобно
//EL: не важно, как при этом устроен antlr
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
public class BlockScheme {
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


/*
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
*/