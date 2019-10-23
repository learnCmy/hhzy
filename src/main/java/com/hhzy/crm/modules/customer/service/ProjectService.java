package com.hhzy.crm.modules.customer.service;

import com.hhzy.crm.common.base.BaseService;
import com.hhzy.crm.modules.customer.entity.Project;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/8/29 14:01
 * @Description:
 */

public interface ProjectService extends BaseService<Project> {

    Map<String ,Object> selectConfig(Long projectId);

    void deleteBatch(List<Long> ids);
}
