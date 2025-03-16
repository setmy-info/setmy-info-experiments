package info.setmy.ann;

import info.setmy.ann.config.LayerConfig;
import info.setmy.ann.config.NetworkConfig;
import info.setmy.ann.csv.CSVRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static info.setmy.ann.FunctionType.SIGMOID;
import static info.setmy.ann.utils.AllUtils.groupByClassType;
import static info.setmy.ann.utils.AllUtils.printData;
import static info.setmy.ann.utils.AllUtils.readAllRecords;
import static info.setmy.ann.utils.AllUtils.splitRandomlyData;
import static info.setmy.ann.utils.AllUtils.toFitData;

public class Application {

    public static void main(String[] args) {

        try {
            Map<Integer, List<CSVRecord>> groupedRecords = groupByClassType(readAllRecords("./src/test/resources/iris.zip/iris.data"));
            List<CSVRecord>[] split = splitRandomlyData(groupedRecords);
            printData(split);

            var networkConfig = NetworkConfig.builder().build()
                    .add(LayerConfig.builder()
                            .name("Input layer")
                            .size(4)
                            .build()
                    ).add(LayerConfig.builder()
                            .name("Hidden layer 1")
                            .size(8)
                            .functionType(SIGMOID)
                            .build())
                    .add(LayerConfig.builder()
                            .name("Hidden layer 2")
                            .size(8)
                            .functionType(SIGMOID)
                            .build())
                    .add(LayerConfig.builder()
                            .name("Output layer")
                            .size(groupedRecords.size())
                            .functionType(SIGMOID)
                            .build())
                    .makeFinal();
            var network = new Network();
            network.configure(networkConfig);

            List<CSVRecord> trainDataRecords = split[0];
            List<CSVRecord> testDataRecords = split[1];
            double[][] trainData = toFitData(trainDataRecords);
            double[][] testData = toFitData(testDataRecords);
            network.fit(trainData, testData, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
