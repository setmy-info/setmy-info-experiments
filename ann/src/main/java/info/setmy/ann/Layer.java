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

    public void forward() {
        for (var neuron : neurons) {
            neuron.forward();
        }
    }
}
