package com.tam.model.NVDCVE;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDBaseMetricV3 {
    private NVDCVSS3 cvssV3;
    private float exploitabilityScore;
    private float impactScore;
}
