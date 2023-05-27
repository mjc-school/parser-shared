package school.mjc.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.mjc.parser.Util.parse;
import static school.mjc.parser.predicate.Dsl.binaryExpression;
import static school.mjc.parser.predicate.Dsl.intLiteral;
import static school.mjc.parser.predicate.Dsl.stringLiteral;
import static school.mjc.parser.predicate.Dsl.variableRef;

public class DslTest {

    @Test
    public void intLiteralDsl_shouldMatchIntegerLiteral() {
        int value = 1;
        Expression expression = new IntegerLiteralExpr(value + "");
        Predicate<Expression> predicate = intLiteral(value);

        assertTrue(predicate.test(expression));
    }

    @Test
    public void intLiteralDsl_shouldNotMatchStringLiteral() {
        Expression expression = new StringLiteralExpr("1");
        Predicate<Expression> predicate = intLiteral(1);

        assertFalse(predicate.test(expression));
    }

    @Test
    public void intLiteralDsl_shouldNotMatchIntLiteralWithAnotherValue() {
        Expression expression = new IntegerLiteralExpr("1");
        Predicate<Expression> predicate = intLiteral(2);

        assertFalse(predicate.test(expression));
    }

    @Test
    public void stringLiteralDsl_shouldMatchStringLiteral() {
        String value = "str";
        Expression expression = new StringLiteralExpr(value);
        Predicate<Expression> predicate = stringLiteral(value);

        assertTrue(predicate.test(expression));
    }

    @Test
    public void stringLiteralDsl_shouldNotMatchIntegerLiteral() {
        Expression expression = new IntegerLiteralExpr("1");
        Predicate<Expression> predicate = stringLiteral("str");

        assertFalse(predicate.test(expression));
    }

    @Test
    public void stringLiteralDsl_shouldNotMatchStringLiteralWithAnotherValue() {
        Expression expression = new StringLiteralExpr("someString");
        Predicate<Expression> predicate = stringLiteral("anotherString");

        assertFalse(predicate.test(expression));
    }

    @Test
    public void variableRefDsl_shouldMatch() {
        Expression expression = new NameExpr("a");
        Predicate<Expression> predicate = variableRef("a");

        assertTrue(predicate.test(expression));
    }

    @Test
    public void variableRefDsl_shouldNotMatchIntegerLiteral() {
        Expression expression = new IntegerLiteralExpr("1");
        Predicate<Expression> predicate = variableRef("a");

        assertFalse(predicate.test(expression));
    }

    @Test
    public void variableRefDsl_shouldNotMatchVarRefWithAnotherName() {
        Expression expression = new NameExpr("a");
        Predicate<Expression> predicate = variableRef("b");

        assertFalse(predicate.test(expression));
    }

    @Test
    public void binaryDsl_shouldMatchExpression() {
        CompilationUnit parse = parse("src/test/resources/java/test/binary/SimpleSum.txt");

        int size = parse.findAll(Expression.class, binaryExpression(
            intLiteral(1),
            BinaryExpr.Operator.PLUS,
            intLiteral(2))
        ).size();

        assertEquals(1, size, "Expected to find 1 expression '1 + 2'");
    }
}
