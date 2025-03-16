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
}
