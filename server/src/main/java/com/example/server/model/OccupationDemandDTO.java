package com.example.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OccupationDemandDTO {
    private String occupationTypeId;    // 职业类型ID
    private String occupationName;     // 职业类型名称
    private Integer demandCount;       // 需求次数
}
