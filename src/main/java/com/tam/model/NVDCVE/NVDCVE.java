package com.tam.model.NVDCVE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDCVE {
    private NVDCVEData cve;
    private NVDConfiguration configurations;
    private NVDImpact impact;
    private String publishedDate;
    private String lastModifiedDate;
}
