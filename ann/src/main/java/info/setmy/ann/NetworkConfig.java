package info.setmy.ann;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class NetworkConfig {

    private final List<LayerConfig> layersConfig = new ArrayList<>();
}
