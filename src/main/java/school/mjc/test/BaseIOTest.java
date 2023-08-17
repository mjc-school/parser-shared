package school.mjc.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ArgumentResolver.class)
public class BaseIOTest {
    protected final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    protected final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    protected final PrintStream originalOut = System.out;
    protected final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    protected void assertOutEquals(String expected) {
        assertEquals(expected, updateLineSpliterators(outContent.toString(StandardCharsets.UTF_8)));

    }

    protected String updateLineSpliterators (String initial) {
        return initial
                .replaceAll("(\n\r)|(\r\n)|(\r)", "\n");
    }

    protected List<String> getOutput() {
        return Arrays.asList(outContent.toString(StandardCharsets.UTF_8).split("(\n\r)|(\r\n)|\r|\n"));
    }

    protected void assertOutput(String... expected) {
        assertOutput(output -> "Program output didn't match expected", expected);
    }

    protected void assertOutput(Function<List<String>, String> errorMessageFunction, String... expected) {
        List<String> output = getOutput();
        assertEquals(Arrays.asList(expected), output,
            () -> errorMessageFunction.apply(output));
    }

    protected void assertOutputIgnoreLineDelimiters(String expected) {
        assertOutputIgnoreLineDelimiters(actualInput -> "Program output didn't match expected", expected);
    }


    protected void assertOutputIgnoreLineDelimiters(Function<String, String> errorMessageFunction, String expected) {
        String actualOutput = String.join("", getOutput());
        assertEquals(expected, actualOutput, () -> errorMessageFunction.apply(actualOutput));
    }
}
