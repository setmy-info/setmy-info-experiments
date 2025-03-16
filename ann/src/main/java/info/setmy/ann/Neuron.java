package info.setmy.ann;

import info.setmy.ann.functions.ActivationFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Neuron {

    private final int index;
    private final ActivationFunction activationFunction;
    private double[] weights;// Neuron weights per input neuron outputs
    private double bias;

    private double delta;

    public double forward(double[] previousLayerOutputs) {
        if (previousLayerOutputs.length != weights.length) {
            throw new RuntimeException("Neurons weights number must be the same with previous layer outputs");
        }
        return activationFunction.func(sum(previousLayerOutputs) + bias);
    }

    private double sum(double[] previousLayerOutputs) {
        double result = 0;
        for (int i = 0; i < weights.length; i++) {
            double input = previousLayerOutputs[i];
            double weight = weights[i];
            result += input * weight;
        }
        return result;
    }
}
