package info.setmy.ann.functions;

import info.setmy.ann.Layer;
import info.setmy.ann.Neuron;

import static java.lang.Math.max;

public final class ReluFunction implements ActivationFunction {

    public static final ActivationFunction RELU_FUNCTION = new ReluFunction();

    @Override
    public double func(Layer layer, Neuron neuron) {
        return func(sum(layer, neuron) + neuron.getBias());
    }

    double func(double input) {
        return max(0, input);
    }
}
