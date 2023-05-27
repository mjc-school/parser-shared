package school.mjc.parser.predicate;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.function.Predicate;

public class TopLevelClassPredicate implements Predicate<ClassOrInterfaceDeclaration> {

    private final String name;

    public TopLevelClassPredicate(String name) {
        this.name = name;
    }

    @Override
    public boolean test(ClassOrInterfaceDeclaration n) {
        return !n.isInterface() &&
                !n.isLocalClassDeclaration() &&
                !n.isInnerClass() &&
                n.getName().getIdentifier().equals(name);
    }
}
