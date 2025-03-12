package info.setmy.ann;

import info.setmy.ann.config.LayerConfig;
import info.setmy.ann.config.NetworkConfig;
import info.setmy.ann.csv.CSVRecord;

import java.io.IOException;
import java.util.List;

import static info.setmy.ann.FunctionType.RELU;
import static info.setmy.ann.utils.AllUtils.groupByClassType;
import static info.setmy.ann.utils.AllUtils.printData;
import static info.setmy.ann.utils.AllUtils.readAllRecords;
import static info.setmy.ann.utils.AllUtils.splitRandomlyData;
import static info.setmy.ann.utils.AllUtils.toFitData;

public class Application {

    public static void main(String[] args) {
        var networkConfig = NetworkConfig.builder().build()
                .add(LayerConfig.builder()
                        .name("Input layer")
                        .size(4)
                        .functionType(RELU)
                        .build()
                ).add(LayerConfig.builder()
                        .name("Hidden layer 1")
                        .size(8)
                        .functionType(RELU)
                        .build())
                .add(LayerConfig.builder()
                        .name("Hidden layer 2")
                        .size(8)
                        .functionType(RELU)
                        .build())
                .add(LayerConfig.builder()
                        .name("Output layer")
                        .size(3)
                        .functionType(RELU)
                        .build())
                .makeFinal();
        try {
            List<CSVRecord>[] split = splitRandomlyData(groupByClassType(readAllRecords("./src/test/resources/iris.zip/iris.data")));
            printData(split);

            var network = new Network();
            network.configure(networkConfig);

            List<CSVRecord> trainData = split[0];
            List<CSVRecord> testData = split[1];
            network.fit(toFitData(trainData), toFitData(testData), 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
