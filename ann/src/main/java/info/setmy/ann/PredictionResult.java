package info.setmy.ann;

import info.setmy.ann.csv.CSVRecord;
import lombok.Builder;

@Builder(toBuilder = true)
public record PredictionResult(
        CSVRecord input,
        double[] output,
        int predictedClassIndex,
        String predictedClassName
) {
}
