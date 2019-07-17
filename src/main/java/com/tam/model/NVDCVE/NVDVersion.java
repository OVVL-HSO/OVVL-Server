package com.tam.model.NVDCVE;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDVersion {
    private List<NVDVersionData> version_data;
}
