package info.setmy.ann.config;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class NetworkConfig {

    @Default
    private final List<LayerConfig> layersConfig = new ArrayList<>();
    @Default
    private final double learningRate = 0.01;

    public NetworkConfig add(LayerConfig layerConfig) {
        layersConfig.add(layerConfig);
        return this;
    }

    public NetworkConfig makeFinal() {
        return this.toBuilder()
                .layersConfig(Collections.unmodifiableList(layersConfig))
                .build();
    }
}
