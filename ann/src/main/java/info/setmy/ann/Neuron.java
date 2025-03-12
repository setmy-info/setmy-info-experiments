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
    // Previous layer neurons outputs
    private double[] inputs;
    // Neuron weights per input neuron outputs
    private double[] weights;
    private double bias;

    //@Deprecated//Do be voed out to Layers
    //private double[] outputs;

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
