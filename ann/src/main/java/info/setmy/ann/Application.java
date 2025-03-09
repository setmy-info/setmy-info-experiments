package info.setmy.ann;

import info.setmy.ann.csv.CSVRecord;

import java.io.IOException;
import java.util.List;

import static info.setmy.ann.Node.Type.RELU;
import static info.setmy.ann.csv.AllUtils.*;

public class Application {

    public static void main(String[] args) {
        try {
            List<CSVRecord>[] split = splitRandomlyData(groupByClassType(readAllRecords("./src/test/resources/iris.zip/iris.data")));
            printData(split);

            var network = new Network();
            // Input layer
            network.addLayer(
                    Layer.builder()
                            .nodes(createNodes(4, RELU))
                            .build()
            );

            // Hidden layers

            // Output layer
            network.addLayer(
                    Layer.builder()
                            .nodes(new Node[3])// How many classes in iris data
                            .build()
            );
            List<CSVRecord> trainData = split[0];
            List<CSVRecord> testData = split[1];
            network.fit(trainData, testData, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
