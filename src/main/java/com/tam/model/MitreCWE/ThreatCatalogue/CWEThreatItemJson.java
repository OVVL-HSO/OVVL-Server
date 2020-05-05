package com.tam.model.MitreCWE.ThreatCatalogue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CWEThreatItemJson {
    @JsonProperty("threatCatalogue")
    private List<CWEThreatItem> threatCatalogue;
}

