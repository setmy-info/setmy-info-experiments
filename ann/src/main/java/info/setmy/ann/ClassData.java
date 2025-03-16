package info.setmy.ann;

import lombok.Builder;

@Builder(toBuilder = true)
public record ClassData(
        int index,
        String name
) {
}
