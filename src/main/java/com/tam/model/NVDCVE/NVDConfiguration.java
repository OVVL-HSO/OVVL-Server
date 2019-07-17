package com.tam.model.NVDCVE;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDConfiguration {
    @JsonProperty("CVE_data_version")
    private String CVE_data_version;
    private List<NVDNode> nodes;
}
