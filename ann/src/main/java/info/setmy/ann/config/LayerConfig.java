package info.setmy.ann.config;

import info.setmy.ann.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class LayerConfig {

    private final String name;
    private final int size;
    private final Type type;
}
