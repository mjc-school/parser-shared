package school.mjc.parser.predicate;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import school.mjc.parser.CollectionUtil;

import java.util.List;
import java.util.Optional;
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

    public static Predicate<VariableDeclarator> declaration(String name) {
        return declarationInternal(name, Optional::isEmpty);
    }

    public static Predicate<VariableDeclarator> declaration(String name, Predicate<Expression> value) {
        return declarationInternal(name, o -> o.map(value::test).orElse(false));
    }

    private static Predicate<VariableDeclarator> declarationInternal(String name,
                                                                     Predicate<Optional<Expression>> value) {
        return expression -> expression.getName().getIdentifier().equals(name) &&
                value.test(expression.getInitializer());
    }

    public static Predicate<AssignExpr> assignment(String name, Predicate<Expression> value) {
        return new AssignmentPredicate(name, value);
    }

    public static Predicate<Expression> binaryExpression(Predicate<Expression> left,
                                                         BinaryExpr.Operator operator,
                                                         Predicate<Expression> right) {
        return expression -> {
            // go into all brackets first
            while (expression.isEnclosedExpr()) {
                expression = expression.asEnclosedExpr().getInner();
            }

            if (!expression.isBinaryExpr()) {
                return false;
            }
            BinaryExpr binaryExpr = expression.asBinaryExpr();
            return binaryExpr.getOperator() == operator &&
                    left.test(binaryExpr.getLeft()) &&
                    right.test(binaryExpr.getRight());
        };
    }

    public static Predicate<VariableDeclarator> isInteger() {
        return vd -> vd.getType().isPrimitiveType() &&
                vd.getType().asPrimitiveType().getType().name().equals("INT");
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
