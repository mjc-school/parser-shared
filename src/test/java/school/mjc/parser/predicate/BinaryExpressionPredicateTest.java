package school.mjc.parser.predicate;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.mjc.parser.Util.parse;
import static school.mjc.parser.predicate.Dsl.binaryExpression;
import static school.mjc.parser.predicate.Dsl.binaryExpressionNotStrict;
import static school.mjc.parser.predicate.Dsl.intLiteral;

class BinaryExpressionPredicateTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "src/test/resources/java/test/binary/SimpleSum.txt",
        "src/test/resources/java/test/binary/SimpleSumWithBrackets.txt"
    })
    public void shouldFindSimpleSum(String path) {
        CompilationUnit parse = parse(path);

        int size = parse.findAll(Expression.class, binaryExpression(
            intLiteral(1),
            BinaryExpr.Operator.PLUS,
            intLiteral(2)
        )).size();

        assertEquals(1, size, "Expected to find '1 + 2' expression");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "src/test/resources/java/test/binary/SimpleSum.txt",
        "src/test/resources/java/test/binary/SimpleSumWithBrackets.txt"
    })
    public void shouldFindSimpleSumInNotStrictMode(String path) {
        CompilationUnit parse = parse(path);

        int size = parse.findAll(Expression.class, binaryExpressionNotStrict(
            intLiteral(2),
            BinaryExpr.Operator.PLUS,
            intLiteral(1)
        )).size();

        assertEquals(1, size, "Expected to find '2 + 1' not strict expression");
    }

    @Test
    public void shouldNotFindNotExistingExpression_wrongOperand() {
        CompilationUnit parse = parse("src/test/resources/java/test/binary/SimpleSum.txt");

        boolean isEmpty = parse.findAll(Expression.class, binaryExpression(
            intLiteral(3),
            BinaryExpr.Operator.PLUS,
            intLiteral(2)
        )).isEmpty();

        assertTrue(isEmpty, "Expected not to find '3 + 2' expression");
    }

    @Test
    public void shouldNotFindNotExistingExpression_wrongOperator() {
        CompilationUnit parse = parse("src/test/resources/java/test/binary/SimpleSum.txt");

        boolean isEmpty = parse.findAll(Expression.class, binaryExpression(
            intLiteral(1),
            BinaryExpr.Operator.MINUS,
            intLiteral(2)
        )).isEmpty();

        assertTrue(isEmpty, "Expected not to find '1 - 2' expression");
    }
}
