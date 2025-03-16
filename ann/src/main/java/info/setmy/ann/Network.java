package info.setmy.ann;

import info.setmy.ann.config.LayerConfig;
import info.setmy.ann.config.NetworkConfig;
import info.setmy.ann.csv.CSVRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static info.setmy.ann.utils.AllUtils.getPredictedClassIndex;
import static info.setmy.ann.utils.AllUtils.nextRandomDouble;
import static info.setmy.ann.utils.AllUtils.toClassName;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Network {

    private final List<Layer> layers = new ArrayList<>();
    private Layer inputLayer;
    private Layer outputLayer;

    // For forward
    private Layer current;

    public Network configure(final NetworkConfig networkConfig) {
        Layer previousLayer = null;
        for (LayerConfig currentLayerConfig : networkConfig.getLayersConfig()) {
            int previousLayerSize = previousLayer != null ? previousLayer.getSize() : currentLayerConfig.size();
            Layer layer = createLayer(previousLayerSize, currentLayerConfig);
            addLayer(layer);
            previousLayer = layer;
        }
        return this;
    }

    private Layer createLayer(int previousLayerSize, LayerConfig layerConfig) {
        boolean isInputLayer = layerConfig.functionType() == null; //first input layer should not have function set.
        Neuron[] neurons = isInputLayer ? null : createNeurons(previousLayerSize, layerConfig);
        Layer layer = Layer.builder()
                .name(layerConfig.name())
                .size(layerConfig.size())
                .neurons(neurons)
                // first layer output is null (it is set from train and test data every time again) and first input layer should not have function set.
                // Other have to set it by number of neurons
                .outputs(isInputLayer ? null : new double[layerConfig.size()])
                .build();
        return layer;
    }

    private Neuron[] createNeurons(int previousLayerSize, LayerConfig layerConfig) {
        int layerSize = layerConfig.size();
        FunctionType functionType = layerConfig.functionType();
        Neuron[] neurons = new Neuron[layerSize];
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
                    .build();
        }
        return neurons;
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

    public void fit(double[][] trainData, double[][] testData, CSVRecord[] trainRecords, CSVRecord[] testRecords, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            System.out.println("Epoch " + (epoch + 1) + " / " + epochs);
            for (CSVRecord record : trainRecords) {
                forwardAndBackwardPropagation(record);
            }
            evaluate(testData);
        }
    }

    private void forwardAndBackwardPropagation(CSVRecord record) {
        double[] output = forward(record.getNetworkInputData());
        var predictedClassIndex = getPredictedClassIndex(output);
        var predictedResult = PredictionResult.builder()
                .input(record)
                .output(output)
                .predictedClassIndex(predictedClassIndex)
                .predictedClassName(toClassName(predictedClassIndex))
                .build();
        // TODO: Backpropagation (error -> lower weight)
        backward(predictedResult);
    }

    private double[] forward(double[] record) {
        inputLayer.setOutputs(record);
        Layer currentLayer = inputLayer.getNext();//Starting from first (hidden layer)
        while (currentLayer != null) {
            double[] previousLayerOutputs = currentLayer.getPrevious().getOutputs();
            currentLayer.forward(previousLayerOutputs);
            currentLayer = currentLayer.getNext();
        }
        return outputLayer.getOutputs();
    }

    private void backward(PredictionResult record) {
        // TODO: Backpropalogic
    }

    private void evaluate(double[][] testData) {
        // For case of need to monitor and check precision, then test on test data with network
        // TODO: Evaluation (for example prediction and precision). Eval network precision with test data.
    }
}
