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
    private double learningRate;

    // For forward
    private Layer current;

    public Network configure(final NetworkConfig networkConfig) {
        this.learningRate = networkConfig.getLearningRate();
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
                .functionType(layerConfig.functionType())
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

    public void fit(CSVRecord[] trainRecords, CSVRecord[] testRecords, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            System.out.println("Epoch " + (epoch + 1) + " / " + epochs);
            forwardRecords(trainRecords);
            evaluate(testRecords);
        }
    }

    private void forwardRecords(CSVRecord[] trainRecords) {
        for (CSVRecord record : trainRecords) {
            forwardAndBackwardPropagation(record);
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
        while (currentLayer != null) {// TODO : until pre-last. Last should be handled differently - SoftMAX.
            double[] previousLayerOutputs = currentLayer.getPrevious().getOutputs();
            currentLayer.forward(previousLayerOutputs);
            currentLayer = currentLayer.getNext();
        }
        return outputLayer.getOutputs();
    }

    private void backward(PredictionResult record) {
        // TODO: possible bug, need to fix
        double[] target = new double[outputLayer.getSize()];
        target[record.predictedClassIndex()] = 1.0;

        // Compute output layer deltas
        for (int i = 0; i < outputLayer.getSize(); i++) {
            Neuron neuron = outputLayer.getNeurons()[i];
            double error = target[i] - outputLayer.getOutputs()[i];
            //neuron.setDelta(error * neuron.getActivationFunction().func(outputLayer.getOutputs()[i]));
        }

        // Backpropagate through hidden layers
        Layer currentLayer = outputLayer.getPrevious();
        while (currentLayer != inputLayer) {
            Layer nextLayer = currentLayer.getNext();
            for (int i = 0; i < currentLayer.getSize(); i++) {
                Neuron neuron = currentLayer.getNeurons()[i];
                double sumError = 0;
                for (Neuron nextNeuron : nextLayer.getNeurons()) {
                    sumError += nextNeuron.getWeights()[i] * nextNeuron.getDelta();
                }
                //neuron.setDelta(sumError * neuron.getActivationFunction().func(currentLayer.getOutputs()[i]));
            }
            currentLayer = currentLayer.getPrevious();
        }

        // Update weights and biases
        currentLayer = inputLayer.getNext();
        while (currentLayer != null) {
            for (Neuron neuron : currentLayer.getNeurons()) {
                for (int j = 0; j < neuron.getWeights().length; j++) {
                    neuron.getWeights()[j] += learningRate * neuron.getDelta() * currentLayer.getPrevious().getOutputs()[j];
                }
                neuron.setBias(neuron.getBias() + learningRate * neuron.getDelta());
            }
            currentLayer = currentLayer.getNext();
        }
    }

    private void evaluate(CSVRecord[] testData) {
        // TODO: recheck
        int correct = 0;
        for (CSVRecord record : testData) {
            double[] output = forward(record.getNetworkInputData());
            int predictedClassIndex = getPredictedClassIndex(output);
            if (predictedClassIndex == record.classType()) {
                correct++;
            }
        }
        double accuracy = (double) correct / testData.length * 100;
        System.out.println("Test Accuracy: " + accuracy + "%");
    }
}
