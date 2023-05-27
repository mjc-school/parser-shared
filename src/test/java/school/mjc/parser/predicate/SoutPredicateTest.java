package school.mjc.parser.predicate;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.mjc.parser.Util.parse;
import static school.mjc.parser.predicate.Dsl.intLiteral;
import static school.mjc.parser.predicate.Dsl.sout;

class SoutPredicateTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "src/test/resources/java/test/sout/CorrectPrint.txt",
        "src/test/resources/java/test/sout/CorrectPrintln.txt"
    })
    public void shouldFindCorrectPrintCall(String path) {
        CompilationUnit parse = parse(path);

        int size = parse.findAll(MethodCallExpr.class, sout().withoutArguments()).size();

        assertEquals(1, size, "Expected to find exactly 1 sout call");
    }

    @Test
    public void shouldFindCorrectPrintCallWithArgument() {
        Path path = Paths.get("src/test/resources/java/test/sout/CorrectPrintlnWithArgument.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(MethodCallExpr.class, sout().withArgument(intLiteral(1))).size();

        assertEquals(1, size, "Expected to find exactly 1 sout call with int '1' argument");
    }

    @Test
    public void shouldFailCorrectPrintCallWithArgument_whenCallDoesNotHaveArguments() {
        Path path = Paths.get("src/test/resources/java/test/sout/CorrectPrintln.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(MethodCallExpr.class, sout().withArgument(intLiteral(1))).size();

        assertEquals(0, size, "Expected not to find sout call with '1' argument");
    }

    @Test
    public void shouldFailCorrectPrintCallWithoutArgument_whenCallHasArguments() {
        Path path = Paths.get("src/test/resources/java/test/sout/CorrectPrintlnWithArgument.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(MethodCallExpr.class, sout().withoutArguments()).size();

        assertEquals(0, size, "Expected not to find sout call without arguments");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "src/test/resources/java/test/sout/IncorrectPrintln-commented.txt",
        "src/test/resources/java/test/sout/IncorrectPrintln-notOut.txt",
        "src/test/resources/java/test/sout/IncorrectPrintln-notSystem.txt",
        "src/test/resources/java/test/sout/IncorrectPrintln-tooManyAccesses.txt",
        "src/test/resources/java/test/sout/IncorrectPrintln-wrongName.txt",
        "src/test/resources/java/test/sout/IncorrectPrintln-noScope.txt",
    })
    public void shouldFailToFindCorrectPrintCall(String path) {
        CompilationUnit parse = parse(path);

        int size = parse.findAll(MethodCallExpr.class, sout()).size();

        assertEquals(0, size, "Expected not to find sout call");
    }
}
