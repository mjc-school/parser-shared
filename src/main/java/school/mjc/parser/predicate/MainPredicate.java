package school.mjc.parser.predicate;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.VoidType;

import java.util.function.Predicate;

public class MainPredicate implements Predicate<MethodDeclaration> {

    public static final String STRING = String.class.getSimpleName();

    @Override
    public boolean test(MethodDeclaration n) {
        if (!(n.isStatic() && n.isPublic() && n.getName().getIdentifier().equals("main"))) {
            return false;
        }
        if (!n.getType().getClass().equals(VoidType.class)) {
            return false;
        }
        return hasSingleStringArrayParameter(n);
    }

    private static boolean hasSingleStringArrayParameter(MethodDeclaration n) {
        NodeList<Parameter> parameters = n.getParameters();
        if (parameters.size() != 1) {
            return false;
        }
        Parameter parameter = parameters.get(0);
        if (parameter.getType().isArrayType()) {
            String argType = parameter.getType().asArrayType()
                    .getComponentType()
                    .asClassOrInterfaceType()
                    .getName()
                    .getIdentifier();
            return STRING.equals(argType);
        } else if (parameter.getType().isClassOrInterfaceType()) {
            ClassOrInterfaceType argType = parameter.getType().asClassOrInterfaceType();
            return parameter.isVarArgs() &&
                    STRING.equals(argType.getName().getIdentifier());
        }
        return false;
    }
}
