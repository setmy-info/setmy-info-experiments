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
    private double[] inputs;    // Previous layer neurons outputs
    private double[] weights;// Neuron weights per input neuron outputs
    private double bias;

    public double forward() {
        if (inputs.length != weights.length) {
            throw new RuntimeException("Neurons must have the same length in inputs and weights");
        }
        //outputs[index] = activationFunction.func(sum() + bias);
        return activationFunction.func(inputs[index]);
    }

    private double sum() {
        double result = 0;
        for (int i = 0; i < inputs.length; i++) {
            result += inputs[i] * weights[i];
        }
        return result;
    }
}
