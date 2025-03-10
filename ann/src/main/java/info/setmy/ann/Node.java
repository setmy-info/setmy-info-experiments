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

    public double sigmoid(double input) {
        return 1.0 / (1.0 + Math.exp(-input));
    }

    public double relu(double input) {
        return Math.max(0, input);
    }

    public double activate(double input) {
        return switch (type) {
            case RELU -> relu(input);
            default -> sigmoid(input);//sigmoid default
        };
    }

}
