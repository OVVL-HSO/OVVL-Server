package com.tam.model.NVDCVE;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDImpact {
    private NVDBaseMetricV3 baseMetricV3;
    private NVDBaseMetricV2 baseMetricV2;
}
