package school.mjc.parser.predicate;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;

import java.util.Optional;
import java.util.function.Predicate;

public class VariableDeclarationPredicate implements Predicate<VariableDeclarator> {

    private Predicate<Optional<Expression>> initializerPredicate = ignored -> true;
    private Predicate<Type> typePredicate = ignored -> true;

    private final String name;

    public VariableDeclarationPredicate(String name) {
        this.name = name;
    }

    @Override
    public boolean test(VariableDeclarator variableDeclarator) {
        return variableDeclarator.getName().getIdentifier().equals(name) &&
            typePredicate.test(variableDeclarator.getType()) &&
            initializerPredicate.test(variableDeclarator.getInitializer());
    }

    public VariableDeclarationPredicate withInitializer(Predicate<Expression> predicate) {
        this.initializerPredicate = init -> init.map(predicate::test).orElse(false);
        return this;
    }

    public VariableDeclarationPredicate withoutInitializer() {
        this.initializerPredicate = Optional::isEmpty;
        return this;
    }

    public VariableDeclarationPredicate ofPrimitiveType(PrimitiveType.Primitive type) {
        this.typePredicate = t -> t.isPrimitiveType() && t.asPrimitiveType().getType() == type;
        return this;
    }

    public VariableDeclarationPredicate ofType(Predicate<Type> typePredicate) {
        this.typePredicate = typePredicate;
        return this;
    }

}
