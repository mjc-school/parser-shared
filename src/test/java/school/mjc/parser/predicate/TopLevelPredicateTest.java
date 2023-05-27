package school.mjc.parser.predicate;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.mjc.parser.Util.parse;
import static school.mjc.parser.predicate.Dsl.topLevelClass;

class TopLevelPredicateTest {

    @Test
    public void shouldFindTopLevelClass() {
        Path path = Paths.get("src/test/resources/java/test/topLevel/code.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(ClassOrInterfaceDeclaration.class, topLevelClass("AClass")).size();

        assertEquals(1, size, "Expected to find exactly 1 top-level class with name AClass");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "NestedClass",
        "AnInterface",
        "LocalClass"
    })
    public void shouldNotFindNotTopLevelClass(String className) {
        Path path = Paths.get("src/test/resources/java/test/topLevel/code.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(ClassOrInterfaceDeclaration.class, topLevelClass(className)).size();

        assertEquals(0, size, "Expected not to find top-level class with name Nested");
    }
}
