package com.hhzy.crm.modules.customer.service.impl;

import com.google.common.collect.Maps;
import com.hhzy.crm.common.base.BaseServiceImpl;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.modules.customer.dao.ProjectMapper;
import com.hhzy.crm.modules.customer.entity.Project;
import com.hhzy.crm.modules.customer.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/8/29 14:01
 * @Description:
 */
@Service
@Transactional
@Slf4j
public class ProjectServiceImpl extends BaseServiceImpl<Project> implements ProjectService {


    @Autowired
    private ProjectMapper projectMapper;


    @Override
    public Map<String, Object> selectConfig(Long projectId) {
        Project project = this.queryById(projectId);
        if(project==null){
            throw new BusinessException("所查询的项目信息不存在");
        }

        //获取居住地址
        String liveAddressConfig = project.getLiveAddressConfig();
        List<String> liveAddressList = Arrays.asList(liveAddressConfig.split(","));

        //获取工作地址
        String workAddressConfig = project.getWorkAddressConfig();
        List<String>  workAddressList = Arrays.asList(workAddressConfig.split(","));

        Map<String, Object> map = Maps.newHashMap();

        map.put("liveAddressList",liveAddressList);
        map.put("workAddressList",workAddressList);
        return map;
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        Example example = new Example(Project.class);
        example.createCriteria().andIn("id",ids);
        projectMapper.deleteByExample(example);
    }
}
