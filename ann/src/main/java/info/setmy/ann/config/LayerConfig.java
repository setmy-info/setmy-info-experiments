package info.setmy.ann.config;

import info.setmy.ann.FunctionType;
import lombok.Builder;

@Builder(toBuilder = true)
public record LayerConfig(
        String name,
        int size,
        FunctionType functionType
) {
}
