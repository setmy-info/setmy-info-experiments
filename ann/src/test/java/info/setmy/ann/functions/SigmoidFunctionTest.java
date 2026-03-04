package info.setmy.ann.functions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class SigmoidFunctionTest {

    SigmoidFunction sigmoid = new SigmoidFunction();

    @Test
    void testSigmoidFunction() {
        assertThat(sigmoid.func(-10.0)).isCloseTo(0.000045397868, within(0.00001));
        assertThat(sigmoid.func(-4.0)).isCloseTo(0.01799, within(0.00001));
        assertThat(sigmoid.func(-2.0)).isCloseTo(0.11920, within(0.00001));
        assertThat(sigmoid.func(-1.0)).isCloseTo(0.26894, within(0.00001));
        assertThat(sigmoid.func(0.0)).isCloseTo(0.5, within(0.00001));
        assertThat(sigmoid.func(1.0)).isCloseTo(0.73106, within(0.00001));
        assertThat(sigmoid.func(2.0)).isCloseTo(0.88080, within(0.00001));
        assertThat(sigmoid.func(4.0)).isCloseTo(0.98201, within(0.00001));
        assertThat(sigmoid.func(10.0)).isCloseTo(0.99995, within(0.00001));
    }

    @Test
    void testSigmoidDerivative() {
        // derivative = output * (1 - output), where output = sigmoid(x)
        assertThat(sigmoid.derivative(0.5)).isCloseTo(0.25, within(0.00001));     // sigmoid(0)
        assertThat(sigmoid.derivative(0.73106)).isCloseTo(0.19661, within(0.0001)); // sigmoid(1)
        assertThat(sigmoid.derivative(0.26894)).isCloseTo(0.19661, within(0.0001)); // sigmoid(-1)
        assertThat(sigmoid.derivative(0.0)).isCloseTo(0.0, within(0.00001));
        assertThat(sigmoid.derivative(1.0)).isCloseTo(0.0, within(0.00001));
    }
}