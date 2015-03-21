package reader;

import com.github.antlrjavaparser.api.Comment;
import com.github.antlrjavaparser.api.body.CatchParameter;
import com.github.antlrjavaparser.api.body.Resource;
import com.github.antlrjavaparser.api.expr.Expression;
import com.github.antlrjavaparser.api.expr.LambdaExpr;
import com.github.antlrjavaparser.api.expr.MethodReferenceExpr;
import com.github.antlrjavaparser.api.stmt.*;
import com.github.antlrjavaparser.api.visitor.VoidVisitorAdapter;
import view.JaviBlockSchemeBlock;
import view.JaviBlockSchemeBlockType;

import java.util.ArrayList;
import java.util.Iterator;

public class JaviBlockSchemeVisitor extends VoidVisitorAdapter
{
    @Override
    public void visit(Comment comment, Object o)
    {

    }

    @Override
    public void visit(CatchParameter catchParameter, Object o)
    {

    }

    @Override
    public void visit(Resource resource, Object o)
    {

    }

    @Override
    public void visit(LambdaExpr lambdaExpr, Object o)
    {

    }

    @Override
    public void visit(MethodReferenceExpr methodReferenceExpr, Object o)
    {

    }

    @Override
    public void visit(IfStmt n, Object arg)
    {
        ArrayList<JaviBlockSchemeBlock> statements = (ArrayList<JaviBlockSchemeBlock>) arg;
        JaviBlockSchemeBlock ifBlock = new JaviBlockSchemeBlock
                (JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeIfStatement, n.getCondition().toString());
        statements.add(ifBlock);
        n.getThenStmt().accept(this,ifBlock.getNestedStatements().get(0));
        if(n.getElseStmt() != null) {
            n.getElseStmt().accept(this, ifBlock.getNestedStatements().get(1));
        }
    }

    @Override
    public void visit(BreakStmt n, Object arg)
    {
        super.visit(n, arg);
    }

    @Override
    public void visit(CatchClause n, Object arg)
    {
        super.visit(n, arg);
    }

    @Override
    public void visit(ForStmt n, Object arg)
    {
        ArrayList<JaviBlockSchemeBlock> statements = (ArrayList<JaviBlockSchemeBlock>) arg;

        Iterator i$;
        Expression e;
        StringBuilder content = new StringBuilder();
        if(n.getInit() != null) {
            i$ = n.getInit().iterator();

            while(i$.hasNext()) {
                e = (Expression)i$.next();
                content.append(e.toString() + "\n");
            }
        }

        if(n.getCompare() != null) {
            content.append(n.getCompare().toString() + "\n");
        }

        if(n.getUpdate() != null) {
            i$ = n.getUpdate().iterator();

            while(i$.hasNext()) {
                e = (Expression)i$.next();
                content.append(e.toString() + "\n");
            }
        }
        JaviBlockSchemeBlock forBlock = new JaviBlockSchemeBlock
                (JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeForStatement, content.toString());
        statements.add(forBlock);

        n.getBody().accept(this, forBlock.getNestedStatements().get(0));
    }

    @Override
    public void visit(ForeachStmt n, Object arg)
    {
        super.visit(n, arg);
    }

    @Override
    public void visit(ReturnStmt n, Object arg)
    {
        ArrayList<JaviBlockSchemeBlock> statements = (ArrayList<JaviBlockSchemeBlock>) arg;
        JaviBlockSchemeBlock expressionBlock = new JaviBlockSchemeBlock
                (JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeReturn, n.getExpr().toString());
        statements.add(expressionBlock);
    }

    @Override
    public void visit(SwitchEntryStmt n, Object arg)
    {
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchStmt n, Object arg)
    {
        super.visit(n, arg);
    }

    @Override
    public void visit(ThrowStmt n, Object arg)
    {
        ArrayList<JaviBlockSchemeBlock> statements = (ArrayList<JaviBlockSchemeBlock>) arg;
        JaviBlockSchemeBlock expressionBlock = new JaviBlockSchemeBlock
                (JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeThrow, n.getExpr().toString());
        statements.add(expressionBlock);
    }

    @Override
    public void visit(TryStmt n, Object arg)
    {
        super.visit(n, arg);
    }

    @Override
    public void visit(WhileStmt n, Object arg)
    {
        super.visit(n, arg);
    }

    @Override
    public void visit(ExpressionStmt n, Object arg)
    {
        ArrayList<JaviBlockSchemeBlock> statements = (ArrayList<JaviBlockSchemeBlock>) arg;
        JaviBlockSchemeBlock expressionBlock = new JaviBlockSchemeBlock
                (JaviBlockSchemeBlockType.JaviBlockSchemeBlockTypeExpression, n.getExpression().toString());
        statements.add(expressionBlock);
    }
}
