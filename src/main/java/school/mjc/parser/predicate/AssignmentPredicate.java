package school.mjc.parser.predicate;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.function.Predicate;

public class AssignmentPredicate implements Predicate<AssignExpr> {

    private final String name;
    private final Predicate<Expression> valuePredicate;

    public AssignmentPredicate(String name, Predicate<Expression> valuePredicate) {
        this.name = name;
        this.valuePredicate = valuePredicate;
    }

    @Override
    public boolean test(AssignExpr n) {
        if (!(n.getTarget() instanceof NameExpr ne) || !(ne.getName().getIdentifier().equals(name))) {
            return false;
        }
        return valuePredicate.test(n.getValue());
    }
}
