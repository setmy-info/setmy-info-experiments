package info.setmy.ann;

import info.setmy.ann.config.LayerConfig;
import info.setmy.ann.config.NetworkConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static info.setmy.ann.utils.AllUtils.nextRandomDouble;

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
            int prevSize = previousLayer == null ? layerConfig.size() : previousLayer.getNeurons().length;
            Layer layer = createLayer(prevSize, previousOutputs, layerConfig);
            addLayer(layer);
            previousOutputs = layer.getOutputs();
            previousLayer = layer;
        }
    }

    private Layer createLayer(int previousLayerSize, double[] previousOutputsAsInputs, LayerConfig layerConfig) {
        int layerSize = layerConfig.size();
        FunctionType functionType = layerConfig.functionType();
        Neuron[] neurons = new Neuron[layerSize];
        double[] outputs = new double[layerSize];
        for (int i = 0; i < layerSize; i++) {
            double[] weights = new double[previousLayerSize];
            for (int w = 0; w < previousLayerSize; w++) {
                weights[w] = -1 + 2 * nextRandomDouble();
            }
            double bias = -1 + 2 * nextRandomDouble();
            neurons[i] = Neuron.builder()
                    .index(i)
                    .activationFunction(
                            functionType != null ? functionType.getActivationFunction() : null
                    )
                    .weights(weights)
                    .bias(bias)
                    .inputs(previousOutputsAsInputs)
                    .build();
        }
        Layer layer = Layer.builder()
                .name(layerConfig.name())
                .neurons(neurons)
                .outputs(outputs)
                .build();
        return layer;
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
        for (int epoch = 0; epoch < epochs; epoch++) {
            System.out.println("Epoch " + (epoch + 1) + " / " + epochs);
            for (double[] record : trainData) {
                forwardAndBackwardPropagation(record);
            }
            evaluate(testData);
        }
    }

    private void forwardAndBackwardPropagation(double[] record) {
        forward(record);
        // TODO: Backpropagation (error -> lower weight)
        backward(record);
    }

    private void forward(double[] record) {
        // TODO:
    }

    private void backward(double[] record) {
        // TODO: Backpropalogic
    }

    private void evaluate(double[][] testData) {
        // For case of need to monitor and check precision, then test on test data with network
        // TODO: Evaluation (for example prediction and precision). Eval network precision with test data.
    }
}
