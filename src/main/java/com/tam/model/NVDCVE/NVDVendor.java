package com.tam.model.NVDCVE;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NVDVendor {
    private List<NVDVendorData> vendor_data;
}
