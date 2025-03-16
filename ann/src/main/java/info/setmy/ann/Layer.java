package info.setmy.ann;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Layer {

    private final String name;
    private final int size;
    private final Layer previous;
    private Layer next;
    private final Neuron[] neurons;
    private double[] outputs;

    public void forward(double[] previousLayerOutputs) {
        for (int i = 0; i < neurons.length; i++) {
            Neuron neuron = neurons[i];
            outputs[i] = neuron.forward(previousLayerOutputs);
        }
    }
}
