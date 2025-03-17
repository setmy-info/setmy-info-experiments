package info.setmy.ann.functions;

import info.setmy.ann.Layer;
import info.setmy.ann.Neuron;

import static java.lang.Math.exp;

public final class SigmoidFunction implements ActivationFunction {

    public static final ActivationFunction SIGMOID_FUNCTION = new SigmoidFunction();

    @Override
    public double func(Layer layer, Neuron neuron) {
        return func(sum(layer, neuron) + neuron.getBias());
    }

    double func(double input) {
        return 1.0 / (1.0 + exp(-input));
    }
}
