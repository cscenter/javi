package new_model;

import com.github.antlrjavaparser.api.body.VariableDeclarator;
import com.github.antlrjavaparser.api.expr.AssignExpr;
import com.github.antlrjavaparser.api.expr.BinaryExpr;
import com.github.antlrjavaparser.api.expr.Expression;
import com.github.antlrjavaparser.api.expr.MethodCallExpr;
import com.github.antlrjavaparser.api.expr.UnaryExpr;
import com.github.antlrjavaparser.api.expr.VariableDeclarationExpr;
import com.github.antlrjavaparser.api.stmt.BreakStmt;
import com.github.antlrjavaparser.api.stmt.CatchClause;
import com.github.antlrjavaparser.api.stmt.ContinueStmt;
import com.github.antlrjavaparser.api.stmt.ForStmt;
import com.github.antlrjavaparser.api.stmt.ForeachStmt;
import com.github.antlrjavaparser.api.stmt.IfStmt;
import com.github.antlrjavaparser.api.stmt.ReturnStmt;
import com.github.antlrjavaparser.api.stmt.Statement;
import com.github.antlrjavaparser.api.stmt.SwitchEntryStmt;
import com.github.antlrjavaparser.api.stmt.SwitchStmt;
import com.github.antlrjavaparser.api.stmt.TryStmt;
import com.github.antlrjavaparser.api.stmt.WhileStmt;
import com.github.antlrjavaparser.api.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    CATCH,
    SWITCH,
    CASE,
    UNARY,
    BINARY
}

//EL: не все классы в иерархии наследования имеют nestedNode => в базовом класса attachInner не нужен
abstract class Node {
    protected Node next;
    protected int level;
    protected int startLine;
    protected int endLine;
    protected int startColumn;
    protected int endColumn;

    protected Node(com.github.antlrjavaparser.api.Node node) {
        startLine = node.getBeginLine();
        endLine = node.getEndLine();
        startColumn = node.getBeginColumn();
        endColumn = node.getEndColumn();
    }

    protected Node(int startLine, int endLine, int startColumn, int endColumn) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
    }

       public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public void setStartLine(int pos) {
        this.startLine = pos;
    }

    public void setEndLine(int pos) {
        this.endLine = pos;
    }

    public void setStartColumn(int pos) {
        this.startColumn = pos;
    }

    public void setEndColumn(int pos) {
        endColumn = pos;
    }

    public boolean contains(Node n) {
        if (this.getStartLine() < n.getStartLine() && this.getEndLine() > n.getEndLine()) {
            return true;
        } else {
            return this.getStartColumn() < n.getStartColumn() && this.getEndColumn() > n.getEndColumn();
        }
    }

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
    public StartNode() {
        super(0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
    }

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
    public EndNode() {
        super(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

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
    private String condition;
    private String body;
    private String else_body;

    public IfNode(IfStmt node) {
        super(node);
        this.condition = node.getCondition().toString();
        this.body = node.getThenStmt().toString();
        this.else_body = node.getElseStmt().toString();
    }

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
    private String body;
    private String condition;

    public ForNode(ForStmt node) {
        super(node);
        List<Expression> init = node.getInit();
        List<Expression> update = node.getUpdate();
        Expression compare = node.getCompare();
        this.body = node.getBody().toString();
        String inits = init.stream().map(Expression::toString).collect(Collectors.joining(" "));
        String updates = update.stream().map(Expression::toString).collect(Collectors.joining(" "));
        condition = inits + " " + compare.toString() + " " + updates;
    }

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
    private String body;
    private String condition;

    public ForEachNode(ForeachStmt node) {
        super(node);
        this.body = node.getBody().toString();
        this.condition = node.getIterable().toString() + " " + node.getVariable().toString();
    }

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

class AssignNode extends Node {
    private String exp;

    public AssignNode(AssignExpr node) {
        super(node.getTarget());
        AssignExpr.Operator op = node.getOperator();
        String operator = "";
        if (op.toString() == "assign") {
            operator = " = ";
        } else if (op.toString() == "plus") {
            operator = "+=";
        } else if (op.toString() == "minus") {
            operator = "-=";
        }

        this.exp = node.getTarget().toString() + " " + operator +
                " " + node.getValue().toString();
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
        builder.append(exp).append("\n");
        return builder.toString();
    }
}

class UnaryNode extends Node {
    private String exp;

    public UnaryNode(UnaryExpr node) {
        super(node);
        UnaryExpr.Operator op = node.getOperator();
        String operator = "";
        if (op.toString() == "preIncrement") {
            operator = "++";
            this.exp = operator + node.getExpr().toString();
        } else if (op.toString() == "posIncrement") {
            operator = "++";
            this.exp = node.getExpr().toString() + operator;
        } else if (op.toString() == "posDecrement") {
            operator = "--";
            this.exp = node.getExpr().toString() + operator;
        } else if (op.toString() == "preDecrement") {
            operator = "--";
            this.exp = operator + node.getExpr().toString();
        }
    }

    @Override
    public NodeType getType() {
        return NodeType.UNARY;
    }

    @Override
    public void attachInner(Node node) {

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append(exp).append("\n");
        return builder.toString();
    }
}

class BinaryNode extends Node {
    private String exp;

    public BinaryNode(BinaryExpr node) {
        super(node);
        BinaryExpr.Operator op = node.getOperator();
        String operator = "";
        if (op.toString() == "equals") {
            operator = " == ";
        } else if (op.toString() == "greater") {
            operator = " > ";
        } else if (op.toString() == "less") {
            operator = " < ";
        } else if (op.toString() == "greaterEquals") {
            operator = " >= ";
        } else if (op.toString() == "lessEquals") {
            operator = " <= ";
        } else if (op.toString() == "notEquals") {
            operator = " != ";
        }

        exp = node.getLeft().toString() + operator + node.getRight().toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.BINARY;
    }

    @Override
    public void attachInner(Node node) {

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append(exp).append("\n");
        return builder.toString();
    }
}

class DeclarationNode extends Node {
    private String exp;

    public DeclarationNode(VariableDeclarationExpr node) {
        super(node);
        String vars = node.getVars().stream().map(VariableDeclarator::toString).collect(Collectors.joining(" "));
        exp = node.getType().toString() + " " + vars;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(exp);
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
    private String method;

    public MethodCallNode(MethodCallExpr node) {
        super(node);

        List<Type> tArgs = node.getTypeArgs();
        List<Expression> arg = node.getArgs();

        String type = "";
        String args = "";

        if (tArgs != null) {
            type = tArgs.stream().map(Type::toString).collect(Collectors.joining(" "));
        }
        if (arg != null) {
            args = arg.stream().map(Expression::toString).collect(Collectors.joining(" "));
        }

        this.method = node.getScope().toString() + type + node.getName() + args;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(method);
        builder.append("\n");
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
    private String expression;

    public ReturnNode(ReturnStmt node) {
        super(node);
        this.expression = node.getExpr().toString();
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

    protected BreakNode(BreakStmt node) {
        super(node);
    }

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

    protected ContinueNode(ContinueStmt node) {
        super(node);
    }

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
    private String condition;
    private String body;

    public WhileNode(WhileStmt node) {
        super(node);
        this.condition = node.getCondition().toString();
        this.body = node.getBody().toString();
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

class TryNode extends Node{
    private Node nestedFirst;
    ArrayList<CatchNode> catchClause = new ArrayList<CatchNode>();
    private String bodyTry;
    private String bodyFinally;

    public TryNode(TryStmt node) {
        super(node);
        for (CatchClause c : node.getCatchs()) {
            catchClause.add(new CatchNode(c));
        }

        bodyTry = node.getTryBlock().toString();
        bodyFinally = node.getFinallyBlock().toString();
    }

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
    private String except;
    private String bodyCatch;

    public CatchNode(CatchClause node) {
        super(node);
        this.except = node.getExcept().toString();
        this.bodyCatch = node.getCatchBlock().toString();
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

class SwitchNode extends Node {
    ArrayList<CaseNode> entries = new ArrayList<>();
    private String selector;
    public SwitchNode(SwitchStmt node) {
        super(node);
        for (SwitchEntryStmt c : node.getEntries()) {
            entries.add(new CaseNode(c));
        }
        this.selector = "switch" + node.getSelector().toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(selector).append(" { \n");

        for (CaseNode i : entries) {
            builder.append(i.toString());
        }

        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append("}\n");
        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.SWITCH;
    }

    @Override
    public void attachInner(Node node) {
//        int i = 0;
//        entries.get(i) = (CaseNode) node;
//        i
//        node.level = this.level + 1;
    }
}

class CaseNode extends Node {
    private Node nestedFirst;
    private String strCase;
    public CaseNode(SwitchEntryStmt node) {
        super(node);
        String stmt = "";
        StringBuilder builder = new StringBuilder();
        List<Statement> body = node.getStmts();
//        stmt = body.stream().map(Statement::toString).collect(Collectors.joining(" "));
//        for (Statement b : body) {
//            builder.append(b.toString());
//        }
        if (node.getLabel() != null) {
            strCase = ("case " + node.getLabel().toString() + " : \n" + builder + "\n");
        } else {
            strCase = ("default : \n" + stmt);
        }
    }

    @Override
    public NodeType getType() {
        return NodeType.CASE;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append(strCase);

        while (tmp != null) {
            builder.append(tmp.toString());
            tmp = tmp.next;
        }

        return builder.toString();
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
    //private com.github.antlrjavaparser.api.Node node;
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


