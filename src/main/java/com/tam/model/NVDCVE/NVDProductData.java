package com.tam.model.NVDCVE;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDProductData {
    private String product_name;
    private NVDVersion version;
}
