package com.tam.model.NVDCVE;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDCVEData {
    private String data_type;
    private String data_format;
    private String data_version;
    @JsonProperty("CVE_data_meta")
    private NVDCVEMetaData CVE_data_meta;
    private NVDAffects affects;
    private NVDProblemType problemtype;
    private NVDReference references;
    private NVDDescription description;
}
