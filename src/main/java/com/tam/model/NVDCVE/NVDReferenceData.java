package com.tam.model.NVDCVE;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDReferenceData {
    private String url;
    private String name;
    private String refsource;
    private List<String> tags;
}
