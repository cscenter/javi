package new_model;

import com.github.antlrjavaparser.api.body.VariableDeclarator;
import com.github.antlrjavaparser.api.body.VariableDeclaratorId;
import com.github.antlrjavaparser.api.expr.AssignExpr;
import com.github.antlrjavaparser.api.expr.BinaryExpr;
import com.github.antlrjavaparser.api.expr.Expression;
import com.github.antlrjavaparser.api.expr.MethodCallExpr;
import com.github.antlrjavaparser.api.expr.UnaryExpr;
import com.github.antlrjavaparser.api.expr.VariableDeclarationExpr;
import com.github.antlrjavaparser.api.stmt.*;
import com.github.antlrjavaparser.api.type.Type;

import javax.print.attribute.standard.Finishings;
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
    BINARY,
    THROW,
    LABEL,
    FINALLY
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
//        if (this.getStartLine() < n.getStartLine() && this.getEndLine() >= n.getEndLine()) {
//            return true;
//        } else {
//            return this.getStartColumn() < n.getStartColumn() && this.getEndColumn() >= n.getEndColumn();
//        }
        if (getStartLine() == n.getStartLine()) {
            if (getEndLine() == n.getEndLine()) {
                return getStartColumn() <= n.getStartColumn() && getEndColumn() >= n.getEndColumn();
            } else {
                return getEndLine() > n.getEndLine();
            }
        } else {
            if (getEndLine() == n.getEndLine()) {
                return getStartLine() < n.getStartLine() && getEndColumn() >= n.getEndColumn();
            } else {
                return getStartLine() < n.getStartLine() && getEndLine() > n.getEndLine();
            }
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

    public IfNode(IfStmt node) {
        super(node);
        this.condition = node.getCondition().toString();
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

        builder.append("if (").append(condition).append(") {\n");
        if (yes != null) {
            Node tmp = yes;
            while (tmp != null) {
                builder.append(tmp);
                tmp = tmp.next;
            }
        }

        if (no != null) {
            for (int i = 0; i < level; ++i) {
                builder.append("--");
            }
            builder.append("} else {\n");
            Node tmp = no;
            while (tmp != null) {
                builder.append(tmp);
                tmp = tmp.next;
            }
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
    private String condition;

    public ForNode(ForStmt node) {
        super(node);
        List<Expression> init = node.getInit();
        List<Expression> update = node.getUpdate();
        Expression compare = node.getCompare();
        String inits = "";
        String updates = "";
        String compares = "";
        if (init != null) {
            inits = init.stream().map(Expression::toString).collect(Collectors.joining(" "));
        }
        if (update != null) {
            updates = update.stream().map(Expression::toString).collect(Collectors.joining(" "));
        }
        if (compare != null) {
            compares = compare.toString();
        }
        condition = inits + " ; " + compares + " ; " + updates;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("for (").append(condition).append(") {\n");
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
    private String condition;

    public ForEachNode(ForeachStmt node) {
        super(node);
        String iterable = "";
        String variable = "";
        if (node.getIterable() != null) {
            iterable = node.getIterable().toString();
        }
        if (node.getVariable() != null) {
            variable = node.getVariable().toString();
        }
        this.condition = variable + " : " + iterable;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("foreach (").append(condition).append(") {\n");
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

class LabelNode extends Node {
    private String label;
    private Node nestedFirst;

    public LabelNode(LabeledStmt node) {
        super(node);
        label = node.getLabel();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append(label).append(":").append("\n");

        while (tmp != null) {
            builder.append(tmp.toString());
            tmp = tmp.next;
        }

        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.LABEL;
    }

    @Override
    public void attachInner(Node node) {
        assert nestedFirst == null;
        nestedFirst = node;
        node.level = this.level + 1;
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

        this.method = node.getScope().toString() + type + "." + node.getName() + "(" + args + ")";
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
        this.expression = "return " + node.getExpr().toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(expression);
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
    private String exp;
    public BreakNode(BreakStmt node) {
        super(node);
        String id = "";
        if (node.getId() != null) {
            id = node.getId();
        }
        this.exp = "break" + " " + id;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(exp).append("\n");
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
    private String exp;
    public ContinueNode(ContinueStmt node) {
        super(node);
        String id = "";
        if (node.getId() != null) {
            id = node.getId();
        }
        this.exp = "continue" + " " + id;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(exp).append("\n");
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

    public WhileNode(WhileStmt node) {
        super(node);
        this.condition = node.getCondition().toString();
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
    private String expTry;
    ArrayList<CatchNode> catchClause = new ArrayList<CatchNode>();

    public FinallyNode getFinallyBlock() {
        return finallyBlock;
    }

    private FinallyNode finallyBlock;

    public TryNode(TryStmt node) {
        super(node);
        this.expTry = "try:";
        if (node.getFinallyBlock() != null) {
            this.finallyBlock = new FinallyNode(node.getFinallyBlock());
            finallyBlock.level = level + 1;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = this.nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("try {\n");
        while (tmp != null) {
            builder.append(tmp);
            tmp = tmp.next;
        }

        if (this.catchClause != null) {
            for (CatchNode catchNode : catchClause) {
                builder.append(catchNode.toString());
            }
        } else {
            builder.append("} ");
        }
        if (this.finallyBlock != null) {
            builder.append(finallyBlock.toString());
        }

        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.TRY;
    }

    @Override
    public void attachInner(Node node) {
        if (nestedFirst == null) {
            nestedFirst = node;
            node.level = this.level + 1;
        } else {
            assert node.getType() == NodeType.CATCH;
            CatchNode catchNode = (CatchNode)node;
            catchClause.add(catchNode);
            node.level = this.level;
        }
    }
}

class FinallyNode extends Node {
    private Node nestedFirst;
    private String expFinally;

    protected FinallyNode(BlockStmt node) {
        super(node);
        this.expFinally = "finally:";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node tmp = nestedFirst;

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append("} finally {\n");
        while (tmp != null) {
            builder.append(tmp.toString());
            tmp = tmp.next;
        }

        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append("} \n");
        return builder.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.FINALLY;
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

    public CatchNode(CatchClause node) {
        super(node);
        this.except = node.getExcept().toString();
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

class ThrowNode extends Node {
    private String exp;

    public ThrowNode(ThrowStmt node) {
        super(node);
        this.exp = "throw " + node.getExpr().toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.THROW;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(exp).append("\n");
        return builder.toString();
    }

    @Override
    public void attachInner(Node node) {
    }
}

class SwitchNode extends Node {
    ArrayList<CaseNode> entries = new ArrayList<>();
    private String selector;
    public SwitchNode(SwitchStmt node) {
        super(node);
        this.selector = "switch" + node.getSelector().toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < level; ++i)
            builder.append("--");

        builder.append(selector).append(" { \n");

        CaseNode tmp = entries.get(0);
        while (tmp != null) {
            builder.append(tmp);
            tmp = (CaseNode)tmp.next;
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
        assert node instanceof CaseNode;
        node.level = this.level + 1;
        entries.add((CaseNode)node);
    }
}

class CaseNode extends Node {
    private Node nestedFirst;
    private String strCase;
    public CaseNode(SwitchEntryStmt node) {
        super(node);
        if (node.getLabel() != null) {
            strCase = ("case " + node.getLabel().toString() + ": \n");
        } else {
            strCase = ("default: \n");
        }
    }

    @Override
    public NodeType getType() {
        return NodeType.CASE;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < level; ++i)
            builder.append("--");
        builder.append(strCase);

        Node tmp = nestedFirst;
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


