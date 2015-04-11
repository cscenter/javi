package new_model;

import com.github.antlrjavaparser.api.body.CatchParameter;
import com.github.antlrjavaparser.api.body.VariableDeclarator;
import com.github.antlrjavaparser.api.expr.AnnotationExpr;
import com.github.antlrjavaparser.api.expr.Expression;
import com.github.antlrjavaparser.api.expr.MethodCallExpr;
import com.github.antlrjavaparser.api.stmt.CatchClause;
import com.github.antlrjavaparser.api.type.Type;

import java.util.ArrayList;
import java.util.List;

enum NodeType {
    START,
    END,
    FOR,
    FOREACH,
    IF,
    WHILE,
    DOWHILE,
    DECLARATION,
    ASSIGN,
    METHOD,
    RETURN,
    BREAK,
    CONTINUE,
    TRY,
    CATCH
}


//EL: все поля должны быть приватными
//EL: не все классы в иерархии наследования имеют nestedNode => в базовом класса attachInner не нужен
abstract class Node {
    protected Node next;
    protected int level;

    // Get the node type
    public abstract NodeType getType();

    // Attach inner node. For: nestedFirst = node, if: yes = node.
    public abstract void attachInner(Node node);

    public Node getNext() {
        return next;
    }

    public void setNext(Node node) {
        this.next = node;
    }
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
    private Node yes;
    private Node no;

    public Node getYes() {
        return yes;
    }

    public void setYes(Node yes) {
        this.yes = yes;
    }

    public Node getNo() {
        return no;
    }

    public void setNo(Node no) {
        this.no = no;
    }

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
    private Node nestedFirst;

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

class ForEachNode extends Node {
    private Node nestedFirst;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("foreach {\n");
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
        return NodeType.FOREACH;
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

class MethodCallNode extends Node {
    private Expression scope;
    private List<Type> typeArgs = new ArrayList<Type>();
    private List<Expression> args = new ArrayList<Expression>();
    private String name;

    public MethodCallNode(MethodCallExpr methodCallExpr) {
        List<Type> tArgs = methodCallExpr.getTypeArgs();
        List<Expression> arg = methodCallExpr.getArgs();

        if (tArgs != null) {
            this.typeArgs =tArgs;
        }
        if (arg != null) {
            this.args = arg;
        }

        this.name = methodCallExpr.getName();
        this.scope = methodCallExpr.getScope();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(scope.toString()).append(".").append(name).append("(");
        for (Expression exp : args) {
            builder.append(exp).append(" ");
        }
        for (Type t : typeArgs) {
            builder.append(t).append(" ");
        }
        builder.append(")\n");
        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.METHOD;
    }

    @Override
    public void attachInner(Node node) {

    }
}

class ReturnNode extends Node{
    private Expression expression;

    public ReturnNode(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("return").append(" ").append(expression);
        builder.append("\n");
        return  builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.RETURN;
    }

    @Override
    public void attachInner(Node node) {
    }
}

class BreakNode extends Node {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("break").append("\n");
        return  builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.BREAK;
    }

    @Override
    public void attachInner(Node node) {

    }
}

class ContinueNode extends Node {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("continue").append("\n");
        return  builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.CONTINUE;
    }

    @Override
    public void attachInner(Node node) {
    }
}

class WhileNode extends Node {
    private Node nestedFirst;
    private Expression condition;

    public WhileNode(Expression condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("while (").append(condition).append(") {\n");
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
        return NodeType.WHILE;
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
    }
}

class DoWhileNode extends Node {
    private Node nestedFirst;
    private Expression condition;

    public DoWhileNode(Expression condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("do {\n");
        while (tmp != null) {
            builder.append(tmp.toString());
            tmp = tmp.next;
        }

        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append("}\n");
        //builder.append("} while(").append(condition).append(") \n");
        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.DOWHILE;
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
    }
}

class TryNode extends Node{
    private Node nestedFirst;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("try {\n");
        while (tmp != null) {
            builder.append(tmp.toString());
            tmp = tmp.next;
        }
        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.TRY;
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
    }
}

class CatchNode extends Node {
    private Node nestedFirst;
    private CatchParameter except;

    public CatchNode(CatchClause catchClause) {
        this.except = catchClause.getExcept();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("} catch (").append(except).append(") {\n");
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
        return NodeType.CATCH;
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
    }
}

// Block scheme consists of one lonely node.
public class BlockScheme {
    private StartNode start = new StartNode();

    public StartNode getStart() {
        return start;
    }

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
