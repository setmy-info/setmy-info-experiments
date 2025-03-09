package info.setmy.ann;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Network {

    private final List<Layer> layers = new ArrayList<>();
    private Layer inputLayer;
    private Layer outputLayer;

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
                // TODO: Forward pass (in -> out)
                forwardPropagation(record);
                // TODO: Backpropagation (error -> lower weight)
                backPropagation(record);
            }
            // For case of need to monitor and check precision, then test on test data with network
            evaluate(testData);
        }
    }

    private void forwardPropagation(double[] layerData) {
        // Input Layer
        for (int i = 0; i < inputLayer.getNodes().length; i++) {
            inputLayer.getNodes()[i].setValue(layerData[i]);  // TODO : korda teha.Sisendi väärtus
        }

        // All other layes
        for (int i = 1; i < layers.size(); i++) {
            Layer currentLayer = layers.get(i);
            Layer previousLayer = layers.get(i - 1);

            // Calculate for every node in next layer
            for (int j = 0; j < currentLayer.getNodes().length; j++) {
                Node node = currentLayer.getNodes()[j];
                double nodeValue = 0;

                // Calc for every node in previous layer with weights
                for (int k = 0; k < previousLayer.getNodes().length; k++) {
                    nodeValue += previousLayer.getNodes()[k].getValue() * node.getWeights()[k];
                }

                nodeValue += node.getBias(); // Add bias
                node.setValue(node.activate()); // Calc activation function
            }
        }
    }

    private void backPropagation(double[] layerData) {
        // TODO: Backpropaga logic
    }

    private void evaluate(double[][] testData) {
        // TODO: Evaluation (for example prediction and precision). Eval network precision with test data.
    }
}
