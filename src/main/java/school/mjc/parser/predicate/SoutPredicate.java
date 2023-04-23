package school.mjc.parser.predicate;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.List;
import java.util.function.Predicate;

public class SoutPredicate implements Predicate<MethodCallExpr> {

    private Predicate<NodeList<Expression>> argumentsPredicate = ignored -> true;

    @Override
    public boolean test(MethodCallExpr n) {
        if (!n.getName().getIdentifier().equals("println")) {
            return false;
        }
        if (n.getScope().get() instanceof FieldAccessExpr fieldAccessExpr) {
            List<Node> childNodes = fieldAccessExpr.getChildNodes();
            if (childNodes.size() < 2) {
                return false;
            }
            if (!(childNodes.get(0) instanceof NameExpr nameExpr) ||
                    !nameExpr.getName().getIdentifier().equals("System")) {
                return false;
            }
            if (!(childNodes.get(1) instanceof SimpleName simpleName) ||
                    !simpleName.getIdentifier().equals("out")) {
                return false;
            }

            return argumentsPredicate.test(n.getArguments());
        }
        return false;
    }

    public SoutPredicate withoutArguments() {
        argumentsPredicate = list -> list.size() == 0;
        return this;
    }

    public SoutPredicate withArgument(Predicate<Expression> argument) {
        argumentsPredicate = list -> list.size() == 1 && argument.test(list.get(0));
        return this;
    }
}
