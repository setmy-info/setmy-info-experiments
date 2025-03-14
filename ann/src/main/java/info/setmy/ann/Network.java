package info.setmy.ann;

import info.setmy.ann.config.LayerConfig;
import info.setmy.ann.config.NetworkConfig;
import info.setmy.ann.functions.ActivationFunction;
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
            int prevSize = previousLayer == null ? layerConfig.getSize() : previousLayer.getNeurons().length;
            //Neuron[] neurons = createNodes(layerConfig.getSize(), layerConfig.getFunctionType(), prevSize, previousOutputs, layerConfig);
            Layer layer = createNodes(prevSize, previousOutputs, layerConfig);
            /*
            Layer layer = Layer.builder()
                    .name(layerConfig.getName())
                    .neurons(neurons)
                    .build();
            */
            addLayer(layer);
            //previousOutputs = neurons[0].getOutputs();// All neurons have same array for output
            previousOutputs = layer.getOutputs();// All neurons have same array for output
            previousLayer = layer;
        }
    }

    private Layer createNodes(int previousLayerSize, double[] previousOutputsAsInputs, LayerConfig layerConfig) {
        int layerSize = layerConfig.getSize();
        FunctionType functionType = layerConfig.getFunctionType();
        Neuron[] neurons = new Neuron[layerSize];
        double[] outputs = new double[layerSize];
        ActivationFunction activationFunction = functionType.getActivationFunction();
        for (int i = 0; i < layerSize; i++) {
            double[] weights = new double[previousLayerSize];
            for (int w = 0; w < previousLayerSize; w++) {
                weights[w] = -1 + 2 * nextRandomDouble();
            }
            double bias = -1 + 2 * nextRandomDouble();
            neurons[i] = Neuron.builder()
                    .index(i)
                    .activationFunction(activationFunction)
                    .weights(weights)
                    .bias(bias)
                    .inputs(previousOutputsAsInputs)
                    //.outputs(outputs)
                    .build();
        }
        Layer layer = Layer.builder()
                .name(layerConfig.getName())
                .neurons(neurons)
                .outputs(outputs)
                .build();
        return layer;
        //return neurons;
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
