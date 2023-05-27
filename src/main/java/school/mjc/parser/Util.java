package school.mjc.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
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
            throw new UncheckedIOException(e);
        }
    }
}
