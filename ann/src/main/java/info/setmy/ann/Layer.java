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
    private final Layer previous;
    private final Neuron[] neurons;
    private Layer next;
    // TODO : moved here from neurons
    private double[] outputs;

    public void forward() {
        if(previous == null) {
            for (var neuron : neurons) {
                outputs[neuron.getIndex()] = neuron.getInputs()[neuron.getIndex()];
            }
        } else {
            for (var neuron : neurons) {
                outputs[neuron.getIndex()] = neuron.forward();
            }
        }
    }
}
