package com.tam.model.NVDCVE;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDCPEMatch {
    private boolean vulnerable;
    private String cpe23Uri;
    private String versionStartIncluding;
    private String versionStartExcluding;
    private String versionEndIncluding;
    private String versionEndExcluding;
}
