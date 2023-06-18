package school.mjc.parser.predicate;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;

import java.util.function.Predicate;

import static school.mjc.parser.CollectionUtil.getSingle;

public class Dsl {
    public static Predicate<Expression> intLiteral(Integer number) {
        return expression -> expression.isIntegerLiteralExpr() &&
            expression.asIntegerLiteralExpr().getValue().equals(number.toString());
    }

    public static Predicate<Expression> stringLiteral(String text) {
        return expression -> expression.isStringLiteralExpr() &&
            expression.asStringLiteralExpr().getValue().equals(text);
    }

    public static Predicate<Expression> variableRef(String name) {
        return expression -> expression.isNameExpr() &&
            expression.asNameExpr().getName().getIdentifier().equals(name);
    }

    public static VariableDeclarationPredicate declaration() {
        return declaration(null);
    }

    public static VariableDeclarationPredicate declaration(String name) {
        return new VariableDeclarationPredicate(name);
    }

    public static BinaryExpressionPredicate binaryExpression(
        Predicate<Expression> left, BinaryExpr.Operator operator, Predicate<Expression> right) {

        return new BinaryExpressionPredicate(left, operator, right);
    }

    public static BinaryExpressionPredicate binaryExpressionNotStrict(
        Predicate<Expression> left, BinaryExpr.Operator operator, Predicate<Expression> right) {

        return binaryExpression(left, operator, right).notStrict();
    }

    public static SoutPredicate sout() {
        return new SoutPredicate();
    }

    public static MainPredicate main() {
        return new MainPredicate();
    }

    public static TopLevelClassPredicate topLevelClass(String name) {
        return new TopLevelClassPredicate(name);
    }

    public static MethodDeclaration findMain(CompilationUnit compilationUnit) {
        return getSingle(compilationUnit.findAll(MethodDeclaration.class, new MainPredicate()),
            i -> "Expected to find single 'public static void main(String[] args)' method, found " + i);
    }

}
