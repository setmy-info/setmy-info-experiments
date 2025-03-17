package info.setmy.ann;

import info.setmy.ann.functions.SoftMaxFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static info.setmy.ann.FunctionType.SOFTMAX;

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
    private FunctionType functionType;

    public void forward(double[] previousLayerOutputs) {
        for (int i = 0; i < neurons.length; i++) {
            Neuron neuron = neurons[i];
            if (previousLayerOutputs.length != neuron.getWeights().length) {
                throw new RuntimeException("Neurons weights number must be the same with previous layer outputs");
            }
            outputs[i] = functionType.getActivationFunction().func(this, neuron);
        }
        postUpdate(outputs);
    }

    private void postUpdate(double[] output) {
        if (next == null && functionType == SOFTMAX) {// Last layer and SOFTMAX used
            SoftMaxFunction softMaxFunction = (SoftMaxFunction) functionType.getActivationFunction();
            softMaxFunction.postUpdate(output);
        }
    }
}
