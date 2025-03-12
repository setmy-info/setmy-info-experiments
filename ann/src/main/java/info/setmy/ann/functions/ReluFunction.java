package info.setmy.ann.functions;

import static java.lang.Math.max;

public final class ReluFunction implements ActivationFunction {

    public static final ActivationFunction RELU_FUNCTION = new ReluFunction();

    @Override
    public double func(double input) {
        return max(0, input);
    }
}
