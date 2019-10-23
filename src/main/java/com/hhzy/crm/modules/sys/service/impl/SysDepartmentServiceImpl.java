package com.hhzy.crm.modules.sys.service.impl;

import com.hhzy.crm.common.base.BaseServiceImpl;
import com.hhzy.crm.modules.sys.dao.SysDepartmentDao;
import com.hhzy.crm.modules.sys.entity.SysDepartment;
import com.hhzy.crm.modules.sys.service.SysDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/1 11:41
 * @Description:
 */
@Service
@Transactional
@Slf4j
public class SysDepartmentServiceImpl extends BaseServiceImpl<SysDepartment> implements SysDepartmentService {

    @Autowired
    private SysDepartmentDao sysDepartmentDao;

    @Override
    public List<SysDepartment> queryDepartment(Long parentId) {
        Example example = new Example(SysDepartment.class);
        example.createCriteria().andEqualTo("parentId",parentId);
        List<SysDepartment> sysDepartments = sysDepartmentDao.selectByExample(example);
        return sysDepartments;
    }

    @Override
    public List<SysDepartment> listAll() {
        Example example = new Example(SysDepartment.class);
        example.createCriteria().andEqualTo("parentId",0l);
        List<SysDepartment> sysDepartmentList = sysDepartmentDao.selectByExample(example);
        getDeptTreeList(sysDepartmentList);
        return sysDepartmentList;
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        Example example = new Example(SysDepartment.class);
        example.createCriteria().andIn("departmentId",ids);
        sysDepartmentDao.deleteByExample(example);
    }

    /**
     * 递归获取子部门
     * @param list
     * @return
     */
    private void getDeptTreeList(List<SysDepartment> list){
        for (SysDepartment sysDepartment : list) {
            List<SysDepartment> sysDepartments = queryDepartment(sysDepartment.getDepartmentId());
            sysDepartment.setChildrenDept(sysDepartments);
            if (CollectionUtils.isNotEmpty(sysDepartments)){
                getDeptTreeList(sysDepartments);
            }
        }
    }
}
