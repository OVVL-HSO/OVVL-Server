package com.tam.model.NVDCVE;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDCVEMetaData {
    @JsonProperty("ID")
    private String ID;
    @JsonProperty("ASSIGNER")
    private String ASSIGNER;
}
