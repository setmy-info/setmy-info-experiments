package info.setmy.ann;

import info.setmy.ann.config.LayerConfig;
import info.setmy.ann.config.NetworkConfig;
import info.setmy.ann.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static info.setmy.ann.FunctionType.SIGMOID;
import static info.setmy.ann.FunctionType.SOFTMAX;
import static info.setmy.ann.utils.AllUtils.getPredictedClassIndex;
import static info.setmy.ann.utils.AllUtils.groupByClassType;
import static info.setmy.ann.utils.AllUtils.readAllRecords;
import static info.setmy.ann.utils.AllUtils.splitRandomlyData;
import static org.assertj.core.api.Assertions.assertThat;

class NetworkTest {

    @Test
    void networkShouldPredictIrisFlowersWithAtLeast70PercentAccuracy() throws Exception {
        URL resource = getClass().getClassLoader().getResource("iris.zip/iris.data");
        File irisFile = new File(resource.toURI());

        Map<Integer, List<CSVRecord>> grouped = groupByClassType(readAllRecords(irisFile));
        List<CSVRecord>[] split = splitRandomlyData(grouped);

        CSVRecord[] trainRecords = split[0].toArray(new CSVRecord[0]);
        CSVRecord[] testRecords = split[1].toArray(new CSVRecord[0]);

        NetworkConfig config = NetworkConfig.builder()
                .learningRate(0.01)
                .build()
                .add(LayerConfig.builder().name("Input").size(4).build())
                .add(LayerConfig.builder().name("Hidden 1").size(8).functionType(SIGMOID).build())
                .add(LayerConfig.builder().name("Hidden 2").size(8).functionType(SIGMOID).build())
                .add(LayerConfig.builder().name("Output").size(grouped.size()).functionType(SOFTMAX).build())
                .makeFinal();

        Network network = new Network().configure(config);
        network.fit(trainRecords, testRecords, 200);

        int correct = 0;
        for (CSVRecord record : testRecords) {
            double[] output = network.forward(record.getNetworkInputData());
            if (getPredictedClassIndex(output) == record.classType()) {
                correct++;
            }
        }
        double accuracy = (double) correct / testRecords.length;

        System.out.printf("Test accuracy: %.1f%% (%d/%d)%n", accuracy * 100, correct, testRecords.length);
        assertThat(accuracy).isGreaterThanOrEqualTo(0.70);
    }
}
