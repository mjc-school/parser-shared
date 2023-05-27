package school.mjc.parser.predicate;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SoutPredicate implements Predicate<MethodCallExpr> {

    private Predicate<NodeList<Expression>> argumentsPredicate = ignored -> true;

    @Override
    public boolean test(MethodCallExpr n) {
        if (!(n.getName().getIdentifier().equals("println") ||
            n.getName().getIdentifier().equals("print"))) {
            return false;
        }

        if (n.getScope().isEmpty() ||
            !(n.getScope().get() instanceof FieldAccessExpr fieldAccessExpr)) {
            return false;
        }

        // verify that before print/println there are `System.out` identifiers
        List<Node> childNodes = buildChain(fieldAccessExpr);
        if (childNodes.size() != 2) {
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

    public List<Node> buildChain(FieldAccessExpr expr) {
        List<Node> result = new ArrayList<>();

        while(true) {
            result.add(0, expr.getChildNodes().get(1));
            Expression scope = expr.getScope();
            if (scope.isFieldAccessExpr()) {
                expr = scope.asFieldAccessExpr();
            } else {
                result.add(0, scope);
                break;
            }
        }
        return result;
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
