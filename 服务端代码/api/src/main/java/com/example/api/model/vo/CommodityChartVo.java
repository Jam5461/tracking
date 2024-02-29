package com.example.api.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommodityChartVo {

    //商品名
    private Integer value;

    //数量
    private String name;

    //运单号
    private String tarcking;

}
