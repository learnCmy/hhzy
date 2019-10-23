package com.hhzy.crm.modules.sys.service;

import com.hhzy.crm.common.base.BaseService;
import com.hhzy.crm.modules.sys.entity.SysDepartment;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/1 11:41
 * @Description:
 */
public interface  SysDepartmentService extends BaseService<SysDepartment> {

    List<SysDepartment> queryDepartment(Long parentId);


    List<SysDepartment> listAll();


    void  deleteByIds(List<Long> ids);
}
