package school.mjc.parser;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilTest {

    @Test
    void shouldParse_whenFileExists() {
        Path path = Paths.get("src/test/resources/java/test/utils/SomeCode.txt");

        CompilationUnit parse = Util.parse(path);

        assertNotNull(parse, "Expected to parse successfully");
    }

    @Test
    void shouldFailParse_whenFileDoesNotExists() {
        Path path = Paths.get("src/test/resources/java/test/utils/NotExistingFile.txt");

        assertThrows(UncheckedIOException.class, () -> Util.parse(path));
    }
}
