package school.mjc.test;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static school.mjc.parser.Util.parse;

public class ArgumentResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(CompilationUnit.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) throws ParameterResolutionException {
        JavaFileSource methodAnnotation = extensionContext.getRequiredTestMethod()
                .getAnnotation(JavaFileSource.class);
        JavaFileSource classAnnotation = extensionContext.getRequiredTestClass()
                .getAnnotation(JavaFileSource.class);
        String path = methodAnnotation != null ? methodAnnotation.value() : classAnnotation.value();
        return parse(path);
    }
}
