package school.mjc.parser;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.*;
import static school.mjc.parser.Asserts.assertNoClassesExceptTopLevel;
import static school.mjc.parser.Asserts.assertNoImports;
import static school.mjc.parser.Asserts.assertNoInitializationBlocks;
import static school.mjc.parser.Asserts.assertNoMethodsExceptMain;
import static school.mjc.parser.Asserts.assertVariablesPrinted;
import static school.mjc.parser.Util.parse;

class AssertsTest {

    @Test
    public void importsAssert_shouldNotFail_whenThereAreNotAsserts() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/Imports_doNotExist.txt");

        assertNoImports(parse);
    }

    @Test
    public void importsAssert_shouldFail_whenThereAreAsserts() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/Imports_exist.txt");

        assertThrows(AssertionFailedError.class,
            () -> assertNoImports(parse));
    }

    @Test
    public void initBlocksAssert_shouldNotFail_whenThereAreNotBlocks() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/InitBlocks_doNotExist.txt");

        assertNoInitializationBlocks(parse);
    }

    @Test
    public void initBlocksAssert_shouldFail_whenThereAreStaticBlocks() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/InitBlocks_existStatic.txt");

        assertThrows(AssertionFailedError.class,
            () -> assertNoInitializationBlocks(parse));
    }

    @Test
    public void initBlocksAssert_shouldFail_whenThereAreBlocks() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/InitBlocks_exist.txt");

        assertThrows(AssertionFailedError.class,
            () -> assertNoInitializationBlocks(parse));
    }

    @Test
    public void onlyMainAssert_shouldNotFail_whenThereIsOnlyMain() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/Methods_onlyMain.txt");

        assertNoMethodsExceptMain(parse);
    }

    @Test
    public void onlyMainAssert_shouldFail_whenThereAreTwoMethods() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/Methods_twoMethods.txt");

        assertThrows(AssertionFailedError.class,
            () -> assertNoMethodsExceptMain(parse));
    }

    @Test
    public void onlyTopLevelClassAssert_shouldNotFail_whenThereIsOnlyTopLevelClass() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/Classes_onlyTopLevel.txt");

        assertNoClassesExceptTopLevel(parse, "Code");
    }

    @Test
    public void onlyTopLevelClassAssert_shouldNotFail_whenNameIsWrong() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/Classes_onlyTopLevel.txt");

        assertThrows(AssertionFailedError.class,
            () -> assertNoClassesExceptTopLevel(parse, "Code2"));
    }

    @Test
    public void onlyTopLevelClassAssert_shouldNotFail_whenThereIsNestedClass() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/Classes_nested.txt");

        assertThrows(AssertionFailedError.class,
            () -> assertNoClassesExceptTopLevel(parse, "Code"));
    }

    @Test
    public void onlyTopLevelClassAssert_shouldNotFail_whenThereIsLocalClass() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/Classes_local.txt");

        assertThrows(AssertionFailedError.class,
            () -> assertNoClassesExceptTopLevel(parse, "Code"));
    }

    @Test
    public void printedVariablesAssert_shouldNotFail_whenVariablesPrintedInSingleExpression() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/Printed_singleExpression.txt");

        assertVariablesPrinted(parse, "a", "b", "c");
    }

    @Test
    public void printedVariablesAssert_shouldNotFail_whenVariablesPrintedInSeveralExpressions() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/Printed_severalExpressions.txt");

        assertVariablesPrinted(parse, "a", "b", "c");
    }

    @Test
    public void printedVariablesAssert_shouldFail_whenOneVariableNotPrinted() {
        CompilationUnit parse = parse("src/test/resources/java/test/asserts/Printed_severalExpressions.txt");

        assertThrows(AssertionFailedError.class,
            () -> assertVariablesPrinted(parse, "a", "b", "d"));
    }
}
