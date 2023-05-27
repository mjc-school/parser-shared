package school.mjc.parser.predicate;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.mjc.parser.Util.parse;
import static school.mjc.parser.predicate.Dsl.findMain;
import static school.mjc.parser.predicate.Dsl.main;

class MainPredicateTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "src/test/resources/java/test/main/ExistingMain.txt",
        "src/test/resources/java/test/main/ExistingMain-varargs.txt",
    })
    public void shouldFindCorrectMainMethod(String path) {
        CompilationUnit parse = parse(path);

        int size = parse.findAll(MethodDeclaration.class, main()).size();

        assertEquals(1, size, "Expected to find exactly 1 correct main method");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "src/test/resources/java/test/main/ExistingMain.txt",
        "src/test/resources/java/test/main/ExistingMain-varargs.txt",
    })
    public void shouldReturnFromFindMainMethod(String path) {
        CompilationUnit parse = parse(path);

        MethodDeclaration main = findMain(parse);

        assertNotNull(main, "Expected found main method not to be null");
    }


    @ParameterizedTest
    @ValueSource(strings = {
        "src/test/resources/java/test/main/IncorrectMain-notMain.txt",
        "src/test/resources/java/test/main/IncorrectMain-notPublic.txt",
        "src/test/resources/java/test/main/IncorrectMain-notStatic.txt",
        "src/test/resources/java/test/main/IncorrectMain-notVoid.txt",
        "src/test/resources/java/test/main/IncorrectMain-primitives.txt",
        "src/test/resources/java/test/main/IncorrectMain-severalArgs.txt",
        "src/test/resources/java/test/main/IncorrectMain-wrongArgs.txt",
        "src/test/resources/java/test/main/IncorrectMain-wrongArgsNotStrings.txt",
        "src/test/resources/java/test/main/IncorrectMain-wrongArgsNotStringsVarargs.txt",
    })
    public void shouldNotFindIncorrectMainMethod(String path) {
        CompilationUnit parse = parse(Paths.get(path));

        int size = parse.findAll(MethodDeclaration.class, main()).size();

        assertEquals(0, size, "Expected not to find any main methods");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "src/test/resources/java/test/main/IncorrectMain-notPublic.txt",
        "src/test/resources/java/test/main/IncorrectMain-twoMains.txt",
    })
    public void shouldThrowException_whenTryingToFindMain(String path) {
        CompilationUnit parse = parse(Paths.get(path));

        assertThrows(IllegalArgumentException.class,
            () -> findMain(parse));
    }
}
