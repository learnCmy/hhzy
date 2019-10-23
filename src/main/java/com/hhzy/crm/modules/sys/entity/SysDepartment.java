package com.hhzy.crm.modules.sys.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/1 11:39
 * @Description:
 */
@Table(name = "sys_department")
@Data
public class SysDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    private Long  parentId;

    private String name;

    @Transient
    private List<SysDepartment> childrenDept;

}
