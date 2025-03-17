package info.setmy.ann;

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
    private double[] weights;// Neuron weights per input neuron outputs
    private double bias;

    private double delta;
}
