package com.hhzy.crm.common.base;

import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseEntity implements Serializable {

    @Transient
    private Integer page=1;

    @Transient
    private Integer pageSize=10;

}