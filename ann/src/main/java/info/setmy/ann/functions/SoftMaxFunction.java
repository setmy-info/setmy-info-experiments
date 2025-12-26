package info.setmy.ann.functions;

import info.setmy.ann.Layer;
import info.setmy.ann.Neuron;

import static java.lang.Math.exp;

public final class SoftMaxFunction implements ActivationFunction {

    public static final ActivationFunction SOFTMAX_FUNCTION = new SoftMaxFunction();

    @Override
    public double func(Layer layer, Neuron neuron) {
        var sumValue = sum(layer, neuron) + neuron.getBias();
        var expValue = func(sumValue);
        return expValue;
    }

    double func(double input) {
        return exp(input);
    }

    public void postUpdate(double[] output) {
        double sumExp = 0.0;
        for (int i = 0; i < output.length; i++) {
            sumExp += output[i];
        }
        for (int i = 0; i < output.length; i++) {
            output[i] = output[i] / sumExp;
        }
    }
}
