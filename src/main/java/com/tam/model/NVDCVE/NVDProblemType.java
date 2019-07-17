package com.tam.model.NVDCVE;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDProblemType {
    private List<NVDProblemTypeData> problemtype_data;
}
