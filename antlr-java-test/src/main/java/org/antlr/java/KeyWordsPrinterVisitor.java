package org.antlr.java;

import com.github.antlrjavaparser.api.Comment;
import com.github.antlrjavaparser.api.Node;
import com.github.antlrjavaparser.api.body.BodyDeclaration;
import com.github.antlrjavaparser.api.body.CatchParameter;
import com.github.antlrjavaparser.api.body.Resource;
import com.github.antlrjavaparser.api.expr.LambdaExpr;
import com.github.antlrjavaparser.api.expr.MethodReferenceExpr;
import com.github.antlrjavaparser.api.stmt.*;
import com.github.antlrjavaparser.api.visitor.VoidVisitor;
import com.github.antlrjavaparser.api.visitor.VoidVisitorAdapter;

import java.util.Stack;

public class KeyWordsPrinterVisitor extends VoidVisitorAdapter
{
    private class NodePosition {
        public int start;
        public int end;
        public GraphicNode node;

        private NodePosition(int start, int end, GraphicNode node) {
            this.start = start;
            this.end = end;
            this.node = node;
        }
    }

    Stack<NodePosition> stack = new Stack<>();

    public KeyWordsPrinterVisitor(BodyDeclaration method) {
        stack.push(new NodePosition(method.getBeginLine(), method.getEndLine(),
                new NodeMethod("Method")));
    }

    public GraphicNode getRoot() {
        while (stack.size() > 1) {
            stack.pop();
        }
        return stack.peek().node;
    }

    public void processNode(Node node, Class cls) {
        assert cls.getSuperclass() == GraphicNode.class;

        try {
            int start = node.getBeginLine();
            int end = node.getEndLine();
            while (!isNested(stack.peek(), start, end)){
                stack.pop();
            }
            assert !stack.empty();

            int level = stack.peek().node.level;
            GraphicNode child = (GraphicNode) cls.getConstructor(int.class).newInstance(level+1);
            stack.peek().node.addNode(child);
            NodePosition position = new NodePosition(start, end, child);
            stack.push(position);

            //Object o = cls.getConstructor(int.class).newInstance(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNested(NodePosition pos, int start, int end) {
        return pos.start < start && pos.end > end;
    }

//    private void printExpression(Node node) {
//        System.out.print(node);
//        System.out.print(node.getBeginLine() + "/");
//        System.out.println(node.getEndLine());
//    }

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

    @Override
    public void visit(IfStmt n, Object arg) {
        processNode(n, NodeIf.class);
        if(n.getElseStmt() != null) {

        }
        super.visit(n, arg);
    }

    @Override
    public void visit(BreakStmt n, Object arg) {
//        System.out.println("Break");
        super.visit(n, arg);
    }

    @Override
    public void visit(CatchClause n, Object arg) {
//        System.out.println("Catch");
        super.visit(n, arg);
    }

    @Override
    public void visit(ForStmt n, Object arg) {
        processNode(n, NodeFor.class);
        super.visit(n, arg);
    }

    @Override
    public void visit(ForeachStmt n, Object arg) {
        processNode(n, NodeForeach.class);
        super.visit(n, arg);
    }

    @Override
    public void visit(ReturnStmt n, Object arg) {
//        System.out.println("Return");
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchEntryStmt n, Object arg) {
//        System.out.println("Switch Entry");
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchStmt n, Object arg) {
//        System.out.println("Switch");
        super.visit(n, arg);
    }

    @Override
    public void visit(ThrowStmt n, Object arg) {
//        System.out.println("Throw");
        super.visit(n, arg);
    }

    @Override
    public void visit(TryStmt n, Object arg) {
//        System.out.println("Try");
        super.visit(n, arg);
    }

    @Override
    public void visit(WhileStmt n, Object arg) {
        processNode(n, NodeWhile.class);
        super.visit(n, arg);
    }
}
