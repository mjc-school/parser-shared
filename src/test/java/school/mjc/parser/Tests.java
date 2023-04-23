package school.mjc.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.metamodel.VariableDeclaratorMetaModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import school.mjc.parser.predicate.MainPredicate;
import school.mjc.parser.predicate.SoutPredicate;
import school.mjc.test.ArgumentResolver;
import school.mjc.test.JavaFileSource;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.mjc.parser.predicate.Dsl.assignment;
import static school.mjc.parser.predicate.Dsl.binaryExpression;
import static school.mjc.parser.predicate.Dsl.declaration;
import static school.mjc.parser.predicate.Dsl.intLiteral;
import static school.mjc.parser.predicate.Dsl.sout;
import static school.mjc.parser.predicate.Dsl.stringLiteral;
import static school.mjc.parser.predicate.Dsl.variableRef;

@ExtendWith(ArgumentResolver.class)
public class Tests {

    @BeforeEach
    void setUp(CompilationUnit unit) {
        int imports = unit.findAll(ImportDeclaration.class).size();
        assertEquals(0, imports, "The task can be solved without any imports");

        int initBlocks = unit.findAll(InitializerDeclaration.class).size();
        assertEquals(0, initBlocks, "Let's avoid using initialization blocks");

        long notSoutMethods = unit.findAll(MethodCallExpr.class)
                .stream()
                .filter(Predicate.not(sout()))
//                .peek(cu -> {
//                    System.out.println(cu);
//                })
                .count();
        assertEquals(0, notSoutMethods, "You don't need any methods except System.out.println");
    }

    @Test
    @JavaFileSource("src/test/java/school/mjc/parser/Code.java")
    void test(CompilationUnit unit) {
        long count = unit.findAll(MethodCallExpr.class)
                .stream()
                .filter(sout().withArgument(stringLiteral("Hello world")))
                .count();

        long count2 = unit.findAll(MethodCallExpr.class)
                .stream().filter(sout().withoutArguments())
                .count();

        long count3 = unit.findAll(MethodCallExpr.class)
                .stream()
                .filter(sout().withArgument(
                        binaryExpression(
                                stringLiteral("Hello world "),
                                BinaryExpr.Operator.PLUS,
                                intLiteral(1)
                        )
                ))
                .count();


        SoutPredicate soutPredicate = sout().withArgument(
                binaryExpression(
                        stringLiteral("Hello world "),
                        BinaryExpr.Operator.PLUS,
                        intLiteral(1)
                )
        );
        long count4 = unit.findAll(MethodCallExpr.class, soutPredicate).size();

        long count5 = unit.findAll(MethodCallExpr.class)
                .stream()
                .filter(Predicate.not(sout()))
                .count();


        System.out.println(count);
        System.out.println(count2);
        System.out.println(count3);
        System.out.println("Not-sout calls " + count5);
    }

    @Test
    @JavaFileSource("src/test/java/school/mjc/parser/Code.java")
    void test2(CompilationUnit unit) {
        long count2 = unit.findAll(MethodDeclaration.class, new MainPredicate())
                .size();

        // VariableDeclarator - 1 for each
        // VariableDeclaratorExpr - 1 for all
//        unit.findAll(VariableDeclarator.class, vde -> true);

//        unit.findAll(AssignExpr.class, i -> {
//            return i.getTarget() instanceof NameExpr ne &&
//                    ne.getName().getIdentifier().equals("a") &&
//                    i.getValue() instanceof AssignExpr bAssign &&
//                    bAssign.getTarget() instanceof NameExpr bNe;
//            // change it somehow, it's awful
//        });

        int assignment = unit.findAll(AssignExpr.class,
                assignment("a", intLiteral(10))).size();
        System.out.println(assignment);

        int souts = unit.findAll(MethodCallExpr.class,
                sout().withArgument(variableRef("a"))).size();

        System.out.println(souts);

//        System.out.println(unit.findAll(VariableDeclarator.class, declaration("a", intLiteral(10))).size());
    }


//    @Test
//    public void t2() {
//
//    }
}
