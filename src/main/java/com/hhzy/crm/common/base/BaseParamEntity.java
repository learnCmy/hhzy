package com.hhzy.crm.common.base;

import lombok.Data;

import javax.persistence.Transient;

@Data
public class BaseParamEntity {


    @Transient
    private Integer page = 1;

    @Transient
    private Integer size = 10;
}
