package school.mjc.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.mjc.parser.predicate.Dsl.main;
import static school.mjc.parser.predicate.Dsl.sout;
import static school.mjc.parser.predicate.Dsl.topLevelClass;

public class Asserts {

    public static void assertNoImports(CompilationUnit parsed) {
        assertEquals(0, parsed.findAll(ImportDeclaration.class).size(),
                "Please don't use 'import' statements");
    }

    public static void assertNoInitializationBlocks(CompilationUnit parsed) {
        assertEquals(0, parsed.findAll(InitializerDeclaration.class).size(),
                "Please don't use declaration blocks statements");
    }

    public static void assertNoMethodsExceptMain(CompilationUnit parsed) {
        assertEquals(0, parsed.findAll(MethodDeclaration.class, main().negate()).size(),
                "Please don't use any methods except main");
    }

    public static void assertNoClassesExceptTopLevel(CompilationUnit parsed, String name) {
        assertEquals(0,
                parsed.findAll(ClassOrInterfaceDeclaration.class, topLevelClass(name).negate()).size(),
                "Please don't create any classes except for the top-level class");
    }

    public static void assertVariablesPrinted(CompilationUnit parsed, String... names) {
        Set<String> printedVariables = parsed.findAll(MethodCallExpr.class, sout())
                .stream()
                .flatMap(mce -> mce.getArguments().stream())
                .flatMap(mce -> mce.findAll(NameExpr.class).stream())
                .map(ne -> ne.getName().getIdentifier())
                .collect(Collectors.toCollection(HashSet::new));

        assertTrue(printedVariables.containsAll(Arrays.asList(names)),
                "Not all variables are printed");
    }
}
