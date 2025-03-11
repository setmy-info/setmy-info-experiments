package info.setmy.ann.functions;

public final class ReluFunction implements ActivationFunction {

    public static final ActivationFunction RELU_FUNCTION = new ReluFunction();

    @Override
    public double func(double input) {
        return Math.max(0, input);
    }
}
