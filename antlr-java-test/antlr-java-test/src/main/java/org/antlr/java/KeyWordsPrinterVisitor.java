package org.antlr.java;

import com.github.antlrjavaparser.api.Comment;
import com.github.antlrjavaparser.api.body.CatchParameter;
import com.github.antlrjavaparser.api.body.Resource;
import com.github.antlrjavaparser.api.expr.LambdaExpr;
import com.github.antlrjavaparser.api.expr.MethodReferenceExpr;
import com.github.antlrjavaparser.api.stmt.*;
import com.github.antlrjavaparser.api.visitor.VoidVisitorAdapter;

public class KeyWordsPrinterVisitor extends VoidVisitorAdapter
{
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
        System.out.println("If");
        super.visit(n, arg);
    }

    @Override
    public void visit(BreakStmt n, Object arg) {
        System.out.println("Break");
        super.visit(n, arg);
    }

    @Override
    public void visit(CatchClause n, Object arg) {
        System.out.println("Catch");
        super.visit(n, arg);
    }

    @Override
    public void visit(ForStmt n, Object arg) {
        System.out.println("For");
        super.visit(n, arg);
    }

    @Override
    public void visit(ForeachStmt n, Object arg) {
        System.out.println("Foreach");
        super.visit(n, arg);
    }

    @Override
    public void visit(ReturnStmt n, Object arg) {
        System.out.println("Return");
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchEntryStmt n, Object arg) {
        System.out.println("Switch Entry");
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchStmt n, Object arg) {
        System.out.println("Switch");
        super.visit(n, arg);
    }

    @Override
    public void visit(ThrowStmt n, Object arg) {
        System.out.println("Throw");
        super.visit(n, arg);
    }

    @Override
    public void visit(TryStmt n, Object arg) {
        System.out.println("Try");
        super.visit(n, arg);
    }

    @Override
    public void visit(WhileStmt n, Object arg) {
        System.out.println("While");
        super.visit(n, arg);
    }
}
