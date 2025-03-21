package info.setmy.ann;

import info.setmy.ann.functions.ActivationFunction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static info.setmy.ann.functions.ReluFunction.RELU_FUNCTION;
import static info.setmy.ann.functions.SigmoidFunction.SIGMOID_FUNCTION;
import static info.setmy.ann.functions.SoftMaxFunction.SOFTMAX_FUNCTION;

@Getter
@RequiredArgsConstructor
public enum FunctionType {

    SIGMOID(SIGMOID_FUNCTION),
    RELU(RELU_FUNCTION),
    SOFTMAX(SOFTMAX_FUNCTION);

    private final ActivationFunction activationFunction;
}
