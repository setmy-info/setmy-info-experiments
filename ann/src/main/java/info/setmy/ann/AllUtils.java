package info.setmy.ann;

import info.setmy.ann.csv.CSVRecord;
import info.setmy.ann.functions.ActivationFunction;
import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static info.setmy.ann.functions.ReluFunction.RELU_FUNCTION;
import static info.setmy.ann.functions.SigmoidFunction.SIGMOID_FUNCTION;
import static java.util.Collections.shuffle;
import static org.apache.commons.csv.CSVFormat.DEFAULT;

@UtilityClass
public class AllUtils {

    private static final double TEST_RATIO = 0.2; // 20% for test data

    public static final Random RANDOM = new Random();

    public static List<CSVRecord> readAllRecords(String fileName) throws IOException {
        return readAllRecords(new File(fileName));
    }

    public static List<CSVRecord> readAllRecords(File file) throws IOException {
        List<CSVRecord> records = new ArrayList<>();
        try (Reader reader = new FileReader(file);
             CSVParser csvParser = CSVParser.parse(reader, DEFAULT)) {

            for (org.apache.commons.csv.CSVRecord record : csvParser) {
                CSVRecord csvRecord = new CSVRecord(
                        Double.parseDouble(record.get(0)),
                        Double.parseDouble(record.get(1)),
                        Double.parseDouble(record.get(2)),
                        Double.parseDouble(record.get(3)),
                        toClassType(record.get(4))
                );
                records.add(csvRecord);
            }
        }
        return records;
    }

    private static int toClassType(String className) {
        return switch (className) {
            case "Iris-setosa" -> 0;
            case "Iris-versicolor" -> 1;
            case "Iris-virginica" -> 2;
            default -> throw new IllegalArgumentException("Unknown class: " + className);
        };
    }

    private static String toClassName(int classType) {
        return switch (classType) {
            case 0 -> "Iris-setosa";
            case 1 -> "Iris-versicolor";
            case 2 -> "Iris-virginica";
            default -> throw new IllegalArgumentException("Unknown class: " + classType);
        };
    }

    public static Map<Integer, List<CSVRecord>> groupByClassType(List<CSVRecord> records) {
        Map<Integer, List<CSVRecord>> groupedRecords = new HashMap<>();
        for (CSVRecord record : records) {
            groupedRecords
                    .computeIfAbsent(record.classType(), k -> new ArrayList<>())
                    .add(record);
        }
        return groupedRecords;
    }

    public static List<CSVRecord>[] splitRandomlyData(Map<Integer, List<CSVRecord>> groupedRecords) {
        List<CSVRecord> trainData = new ArrayList<>();
        List<CSVRecord> testData = new ArrayList<>();
        Random random = new Random();
        for (Map.Entry<Integer, List<CSVRecord>> entry : groupedRecords.entrySet()) {
            List<CSVRecord> records = new ArrayList<>(entry.getValue());
            shuffle(records, random);
            int testSize = (int) (records.size() * TEST_RATIO);
            testData.addAll(records.subList(0, testSize));
            trainData.addAll(records.subList(testSize, records.size()));
        }
        return new List[]{trainData, testData};
    }

    public static double[] toLayerData(CSVRecord record) {
        return new double[]{
                record.sepalLength(),
                record.sepalWidth(),
                record.petalLength(),
                record.petalWidth()
        };
    }

    public static void printData(List<CSVRecord>[] split) {
        List<CSVRecord> trainData = split[0];
        List<CSVRecord> testData = split[1];

        System.out.println("Training data (" + trainData.size() + "):");
        trainData.forEach(System.out::println);

        System.out.println("\nTest data (" + testData.size() + "):");
        testData.forEach(System.out::println);
    }

    public static Neuron[] createNodes(int layerSize, Type type, int previousLayerSize, double[] previousOutputsAsInputs) {
        Neuron[] neurons = new Neuron[layerSize];
        double[] outputs = new double[layerSize];
        ActivationFunction activationFunction = getActivationFunction(type);
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
                    .outputs(outputs)
                    .build();
        }
        return neurons;
    }

    public static double nextRandomDouble() {
        return RANDOM.nextDouble();
    }

    public static double[][] toFitData(List<CSVRecord> trainData) {
        var result = new double[trainData.size()][];
        for (int i = 0; i < trainData.size(); i++) {
            result[i] = trainData.get(i).getLayerData();
        }
        return result;
    }

    public static ActivationFunction getActivationFunction(Type type) {
        return switch (type) {
            case RELU -> RELU_FUNCTION;
            default -> SIGMOID_FUNCTION;//sigmoid default
        };
    }
}
