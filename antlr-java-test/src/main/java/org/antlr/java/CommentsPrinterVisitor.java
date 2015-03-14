 package org.antlr.java;

import com.github.antlrjavaparser.api.*;
import com.github.antlrjavaparser.api.body.*;
import com.github.antlrjavaparser.api.expr.*;
import com.github.antlrjavaparser.api.stmt.*;
import com.github.antlrjavaparser.api.type.*;
import com.github.antlrjavaparser.api.visitor.VoidVisitorAdapter;

public class CommentsPrinterVisitor extends VoidVisitorAdapter {
    @Override
    public void visit(Comment n, Object arg) {
        System.out.println(n.getContent());
    }

    private void visitComments(Node n, Object arg) {
        if (n.getBeginComments() != null) {
            for (Comment comment : n.getBeginComments()) {
                comment.accept(this, arg);
            }
        }

        if (n.getEndComments() != null) {
            for (Comment comment : n.getEndComments()) {
                comment.accept(this, arg);
            }
        }
    }

    @Override
    public void visit(CatchParameter n, Object arg) {
        visitComments(n, arg);
    }

    @Override
    public void visit(Resource n, Object arg) {
        visitComments(n, arg);
    }

    @Override
    public void visit(LambdaExpr n, Object arg) {
        visitComments(n, arg);
    }

    @Override
    public void visit(MethodReferenceExpr n, Object arg) {
        visitComments(n, arg);
    }

    @Override
    public void visit(AnnotationDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(AnnotationMemberDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ArrayAccessExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ArrayCreationExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ArrayInitializerExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(AssertStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(AssignExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(BinaryExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(BlockStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(BooleanLiteralExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(BreakStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(CastExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(CatchClause n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(CharLiteralExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ClassExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ClassOrInterfaceType n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(CompilationUnit n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ConditionalExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ConstructorDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ContinueStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(DoStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(DoubleLiteralExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(EmptyMemberDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(EmptyStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(EmptyTypeDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(EnclosedExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(EnumConstantDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(EnumDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ExpressionStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(FieldAccessExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(FieldDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ForeachStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ForStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(IfStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ImportDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(InitializerDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(InstanceOfExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(IntegerLiteralExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(IntegerLiteralMinValueExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(LabeledStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(LongLiteralExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(LongLiteralMinValueExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(MarkerAnnotationExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(MemberValuePair n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodCallExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(NameExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(NormalAnnotationExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(NullLiteralExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ObjectCreationExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(PackageDeclaration n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(Parameter n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(PrimitiveType n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(QualifiedNameExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ReferenceType n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ReturnStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(SingleMemberAnnotationExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(StringLiteralExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(SuperExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchEntryStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(SynchronizedStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ThisExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(ThrowStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(TryStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(TypeDeclarationStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(TypeParameter n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(UnaryExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(VariableDeclarationExpr n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(VariableDeclarator n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(VariableDeclaratorId n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(VoidType n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(WhileStmt n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }

    @Override
    public void visit(WildcardType n, Object arg) {
        visitComments(n, arg);
        super.visit(n, arg);
    }
}
