package info.setmy.ann;

import info.setmy.ann.csv.CSVRecord;

import java.io.IOException;
import java.util.List;

import static info.setmy.ann.csv.AllUtils.*;

public class Application {
    public static void main(String[] args) {
        try {
            List<CSVRecord>[] split = splitRandomlyData(groupByClassType(readAllRecords("./src/test/resources/iris.zip/iris.data")));
            List<CSVRecord> trainData = split[0];
            List<CSVRecord> testData = split[1];
            printData(split);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
