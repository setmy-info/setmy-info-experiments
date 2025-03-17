package info.setmy.ann.functions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ReluFunctionTest {

    ReluFunction relu;

    @Test
    void testReluFunction() {
        relu = new ReluFunction();

        assertThat(relu.func(-1.0)).isCloseTo(0.0, within(0.0001));
        assertThat(relu.func(-2.0)).isCloseTo(0.0, within(0.0001));
        assertThat(relu.func(1.0)).isCloseTo(1.0, within(0.0001));
        assertThat(relu.func(2.0)).isCloseTo(2.0, within(0.0001));
    }
}