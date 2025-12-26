package info.setmy.ann.functions;

import info.setmy.ann.Layer;
import info.setmy.ann.Neuron;

public interface ActivationFunction {

    double func(Layer layer, Neuron neuron);

    default double sum(Layer layer, Neuron neuron) {
        return sum(
                layer.getPrevious().getOutputs(),
                neuron.getWeights()
        );
    }

    default double sum(double[] inputs, double[] weights) {
        double result = 0;
        for (int i = 0; i < inputs.length; i++) {
            double input = inputs[i];
            double weight = weights[i];
            result += input * weight;
        }
        return result;
    }
}
