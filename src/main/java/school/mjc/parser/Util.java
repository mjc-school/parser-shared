package school.mjc.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Util {

    public static CompilationUnit parse(String path) {
        return parse(Paths.get(path));
    }

    public static CompilationUnit parse(Path path) {
        try {
            return StaticJavaParser.parse(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompilationUnit parse(InputStream stream) {
        return StaticJavaParser.parse(stream);
    }

//    public static CompilationUnit parseMany(String path) {
//        try {
//            CompilationUnit parse = StaticJavaParser.parse(Paths.get(path));
////            parse.findAll(ImportDeclaration.class, id -> id.getName().())
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
}
