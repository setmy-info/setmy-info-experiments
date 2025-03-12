package info.setmy.ann.functions;

public final class PassFunction implements ActivationFunction {

    public static final ActivationFunction PASS_FUNCTION = new PassFunction();

    @Override
    public double func(double input) {
        return input;
    }
}
