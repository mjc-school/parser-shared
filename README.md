# General info

This is a shared library to verify code written by students in the scope of MJC School (https://mjc.school).

Library works by parsing Java source file into abstract syntax tree, and then validating it.

## Usage sample

First, parse the Java file into:

```
Path path = Paths.get("src/test/resources/java/test/topLevel/code.txt");
CompilationUnit parse = school.mjc.parser.Util.parse(path);
```

Then you can validate compilation unit using standard library methods:
```
List<VariableDeclarationExpr> list = parse.findAll(VariableDeclarationExpr.class); // to find all variable declarations
List<VariableDeclarationExpr> list = parse.findAll(VariableDeclarationExpr.class, 
    vde -> vde.getVariables().size() == 1);  // to find all variable declarations, when a single variable is declared
```

See `com.github.javaparser.ast.expr.Expression` and `com.github.javaparser.ast.Node` and their subclasses for more details.

Or you can use DSL provided by this library.

To find a single main method declared in class:
```
MethodDeclaration main = findMain(parse);
```

To find all `System.out.println` calls with a `1` argument:
```
parse.findAll(MethodCallExpr.class, sout().withArgument(intLiteral(1)));
```

To find all binary `1 + 2` expression:
```
parse.findAll(Expression.class, binaryExpression(
    intLiteral(3),
    BinaryExpr.Operator.PLUS,
    intLiteral(2)
))
```

For more details and examples, see `school.mjc.parser.predicate.DSL` and unit tests.

### Asserts

Library allows to verify, that the student didn't use any forbidden syntax structure:

To verify that the parsed class doesn't have any imports:
```
assertNoImports(parse);
```

For more details and examples, see `school.mjc.parser.Asserts` and unit tests.
