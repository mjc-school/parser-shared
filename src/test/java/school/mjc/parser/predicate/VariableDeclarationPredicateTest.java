package school.mjc.parser.predicate;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.PrimitiveType;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.mjc.parser.Util.parse;
import static school.mjc.parser.predicate.Dsl.declaration;
import static school.mjc.parser.predicate.Dsl.intLiteral;

class VariableDeclarationPredicateTest {

    @Test
    void shouldFindVariableDeclarationByName() {
        Path path = Paths.get("src/test/resources/java/test/variableDeclaration/IntDeclaration.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(VariableDeclarator.class, declaration("a")).size();

        assertEquals(1, size, "Expected to find single 'a' variable");
    }

    @Test
    void shouldFindVariableDeclarationByNameExplicitlyWithoutInitializer() {
        Path path = Paths.get("src/test/resources/java/test/variableDeclaration/IntDeclaration.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(VariableDeclarator.class,
            declaration("a").withoutInitializer()).size();

        assertEquals(1, size, "Expected to find single 'a' variable");
    }

    @Test
    void shouldFindVariableDeclarationByNameAndType() {
        Path path = Paths.get("src/test/resources/java/test/variableDeclaration/IntDeclaration.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(VariableDeclarator.class,
            declaration("a").ofPrimitiveType(PrimitiveType.Primitive.INT)).size();

        assertEquals(1, size, "Expected to find single int 'a' variable");
    }

    @Test
    void shouldNotFindVariableWithIncorrectName() {
        Path path = Paths.get("src/test/resources/java/test/variableDeclaration/IntDeclaration.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(VariableDeclarator.class,
            declaration("b")).size();

        assertEquals(0, size, "Expected not to find 'b' variable");
    }

    @Test
    void shouldNotFindVariableWithIncorrectType() {
        Path path = Paths.get("src/test/resources/java/test/variableDeclaration/IntDeclaration.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(VariableDeclarator.class,
            declaration("a").ofPrimitiveType(PrimitiveType.Primitive.SHORT)).size();

        assertEquals(0, size, "Expected not to find short 'a' variable");
    }

    @Test
    void shouldFindVariableDeclarationByNameAndInitializer() {
        Path path = Paths.get("src/test/resources/java/test/variableDeclaration/IntDeclarationWithInitializer.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(VariableDeclarator.class,
            declaration("a").withInitializer(intLiteral(1))).size();

        assertEquals(1, size, "Expected to find single 'int a = 1;'");
    }

    @Test
    void shouldNotFindVariableDeclarationByNameAndIncorrectInitializer() {
        Path path = Paths.get("src/test/resources/java/test/variableDeclaration/IntDeclarationWithInitializer.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(VariableDeclarator.class,
            declaration("a").withInitializer(intLiteral(2))).size();

        assertEquals(0, size, "Expected not to find 'int a = 2;' variable");
    }

    @Test
    void shouldNotFindVariableExplicitlyWithoutInitializer() {
        Path path = Paths.get("src/test/resources/java/test/variableDeclaration/IntDeclarationWithInitializer.txt");
        CompilationUnit parse = parse(path);

        int size = parse.findAll(VariableDeclarator.class,
            declaration("a").withoutInitializer()).size();

        assertEquals(0, size, "Expected not to find 'int a' variable without initializer");
    }

}
