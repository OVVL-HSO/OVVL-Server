package com.tam.model.NVDCVE;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDNode {
    private boolean negate;
    private String operator;
    private List<NVDCPEMatch> cpe_match;
    private List<NVDNode> children;
}
