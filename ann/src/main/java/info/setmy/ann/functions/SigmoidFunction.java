package info.setmy.ann.functions;

public final class SigmoidFunction implements ActivationFunction {

    public static final ActivationFunction SIGMOID_FUNCTION = new ReluFunction();

    @Override
    public double func(double input) {
        return 1.0 / (1.0 + Math.exp(-input));
    }
}
