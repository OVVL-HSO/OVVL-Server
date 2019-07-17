package com.tam.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@Builder
public class CPEItem {
    @Id
    private int id;
    private String cpeName;
    private String title;
    private List<CPEReference> references;
    private String cpe23Name;
}
