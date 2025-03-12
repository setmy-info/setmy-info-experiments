package info.setmy.ann.csv;

public record CSVRecord(
        double sepalLength,
        double sepalWidth,
        double petalLength,
        double petalWidth,
        String className,
        int classType
) {
    public double[] getLayerData() {
        return new double[]{
                sepalLength,
                sepalWidth,
                petalLength,
                petalWidth,
        };
    }
}
