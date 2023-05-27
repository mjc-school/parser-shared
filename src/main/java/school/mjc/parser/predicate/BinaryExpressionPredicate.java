package school.mjc.parser.predicate;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;

import java.util.function.Predicate;

public class BinaryExpressionPredicate implements Predicate<Expression> {

    private final BinaryExpr.Operator operator;
    private final Predicate<Expression> left;
    private final Predicate<Expression> right;

    private boolean strict = true;

    public BinaryExpressionPredicate(Predicate<Expression> left,
                                     BinaryExpr.Operator operator,
                                     Predicate<Expression> right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean test(Expression expression) {
        // don't match expression if it's wrapped in brackets to prevent "finding" the same expression many times
        if (expression.isBinaryExpr() || expression.isEnclosedExpr()) {
            if (expression.getParentNode().isPresent() &&
                expression.getParentNode().get() instanceof EnclosedExpr) {
                return false;
            }
        }

        while (expression.isEnclosedExpr()) {
            expression = expression.asEnclosedExpr().getInner();
        }

        if (!expression.isBinaryExpr()) {
            return false;
        }
        BinaryExpr binaryExpr = expression.asBinaryExpr();
        if (binaryExpr.getOperator() != operator) {
            return false;
        }
        return checkOperands(binaryExpr);
    }

    private boolean checkOperands(BinaryExpr binaryExpr) {
        if (strict) {
            return left.test(binaryExpr.getLeft()) && right.test(binaryExpr.getRight());
        } else {
            return (left.test(binaryExpr.getLeft()) && right.test(binaryExpr.getRight())) ||
                (right.test(binaryExpr.getLeft()) && left.test(binaryExpr.getRight()));
        }
    }

    public BinaryExpressionPredicate notStrict() {
        strict = false;
        return this;
    }
}
