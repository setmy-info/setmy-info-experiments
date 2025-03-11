package info.setmy.ann;

import info.setmy.ann.config.NetworkConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static info.setmy.ann.AllUtils.createNodes;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Network {

    private final List<Layer> layers = new ArrayList<>();
    private Layer inputLayer;
    private Layer outputLayer;

    // For forward
    private Layer current;

    public void configure(final NetworkConfig networkConfig) {
        Layer previousLayer = null;
        double[] previousOutputs = null;
        for (var layerConfig : networkConfig.getLayersConfig()) {
            int prevSize = previousLayer == null ? layerConfig.getSize() : previousLayer.getNeurons().length;
            Neuron[] neurons = createNodes(layerConfig.getSize(), layerConfig.getType(), prevSize, previousOutputs);
            Layer layer = Layer.builder()
                    .name(layerConfig.getName())
                    .neurons(neurons)
                    .build();
            addLayer(layer);
            previousOutputs = neurons[0].getOutputs();// All neurons have same array for output
            previousLayer = layer;
        }
    }

    public boolean addLayer(Layer layer) {
        layer = layer.toBuilder()
                .previous(outputLayer)
                .build();
        if (layers.isEmpty()) {
            inputLayer = layer;
        }
        if (outputLayer != null) {
            outputLayer.setNext(layer);
        }
        outputLayer = layer;
        return layers.add(layer);
    }

    public void fit(double[][] trainData, double[][] testData, int epochs) {
        // TODO : forward and backward propa.
        for (int epoch = 0; epoch < epochs; epoch++) {
            System.out.println("Epoch " + (epoch + 1) + " / " + epochs);
            for (double[] record : trainData) {
                forwardAndBackwardPropagation(record);
            }
            // For case of need to monitor and check precision, then test on test data with network
            evaluate(testData);
        }
    }

    private void forwardAndBackwardPropagation(double[] record) {
        forward(record);
        // TODO: Backpropagation (error -> lower weight)
        backward(record);
    }

    private void forward(double[] record) {
        forwardFirst(record);
        forwardOthers(record);
    }

    private void forwardFirst(double[] record) {
        current = inputLayer;
        for (Neuron neuron : current.getNeurons()) {
            neuron.setInputs(record);
        }
        current.forward();
        current = current.getNext();
    }

    private void forwardOthers(double[] record) {
        while (current != null) {
            current.forward();
            current = current.getNext();
        }
    }

    private void backward(double[] record) {
        // TODO: Backpropalogic
    }

    private void evaluate(double[][] testData) {
        // TODO: Evaluation (for example prediction and precision). Eval network precision with test data.
    }
}
