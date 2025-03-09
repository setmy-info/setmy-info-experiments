package info.setmy.ann;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Node {

    private double value;
    private double bias;
    private double[] weights;
    private double delta;
    private final Type type;

    public double sigmoid() {
        return 1.0 / (1.0 + Math.exp(-value));
    }

    public double relu() {
        return Math.max(0, value);
    }

    public double activate() {
        return switch (type) {
            case RELU -> relu();
            default -> sigmoid();//sigmoid default
        };
    }

    public enum Type {
        SIGMOID,
        RELU;
    }
}
