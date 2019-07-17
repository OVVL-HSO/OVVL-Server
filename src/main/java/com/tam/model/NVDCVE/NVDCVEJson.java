package com.tam.model.NVDCVE;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NVDCVEJson {
    @JsonProperty("CVE_data_type")
    private String CVE_data_type;

    @JsonProperty("CVE_data_format")
    private String CVE_data_format;

    @JsonProperty("CVE_data_version")
    private String CVE_data_version;

    @JsonProperty("CVE_data_numberOfCVEs")
    private String CVE_data_numberOfCVEs;

    @JsonProperty("CVE_data_timestamp")
    private String CVE_data_timestamp;

    @JsonProperty("CVE_Items")
    private List<NVDCVE> CVE_Items;
}
