package info.setmy.ann.functions;

import info.setmy.makrdown.Transpiler;
import org.junit.jupiter.api.Test;

class TranspilerTest {

    Transpiler transpiler = Transpiler.getInstance();

    @Test
    void transpile() {
        transpiler.transpile("./src/test/resources/test.md", "./target/test.html");
    }
}
