package info.setmy.ann.csv;

import info.setmy.ann.Node;
import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import static java.util.Collections.shuffle;
import static org.apache.commons.csv.CSVFormat.DEFAULT;

@UtilityClass
public class AllUtils {

    private static final double TEST_RATIO = 0.2; // 20% for test data

    public List<CSVRecord> readAllRecords(String fileName) throws IOException {
        return readAllRecords(new File(fileName));
    }

    public List<CSVRecord> readAllRecords(File file) throws IOException {
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

    private int toClassType(String className) {
        return switch (className) {
            case "Iris-setosa" -> 0;
            case "Iris-versicolor" -> 1;
            case "Iris-virginica" -> 2;
            default -> throw new IllegalArgumentException("Unknown class: " + className);
        };
    }

    public Map<Integer, List<CSVRecord>> groupByClassType(List<CSVRecord> records) {
        Map<Integer, List<CSVRecord>> groupedRecords = new HashMap<>();
        for (CSVRecord record : records) {
            groupedRecords
                    .computeIfAbsent(record.classType(), k -> new ArrayList<>())
                    .add(record);
        }
        return groupedRecords;
    }

    public List<CSVRecord>[] splitRandomlyData(Map<Integer, List<CSVRecord>> groupedRecords) {
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

    public double[] toLayerData(CSVRecord record) {
        return new double[]{
                record.sepalLength(),
                record.sepalWidth(),
                record.petalLength(),
                record.petalWidth()
        };
    }

    public void printData(List<CSVRecord>[] split) {
        List<CSVRecord> trainData = split[0];
        List<CSVRecord> testData = split[1];

        System.out.println("Training data (" + trainData.size() + "):");
        trainData.forEach(System.out::println);

        System.out.println("\nTest data (" + testData.size() + "):");
        testData.forEach(System.out::println);
    }

    public Node[] createNodes(int size, Node.Type type) {
        Node[] result = new Node[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            double randomBias = -1 + 2 * random.nextDouble();
            double[] weights = new double[size];
            for (int j = 0; j < size; j++) {
                weights[j] = -1 + 2 * random.nextDouble();
            }
            result[i] = Node.builder()
                    .type(type)
                    .bias(randomBias)
                    .weights(weights)
                    .build();
        }
        return result;
    }
}
