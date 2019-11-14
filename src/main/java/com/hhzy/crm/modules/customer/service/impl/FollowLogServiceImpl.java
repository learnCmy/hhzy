package com.hhzy.crm.modules.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.modules.customer.dao.CustomerMapper;
import com.hhzy.crm.modules.customer.dao.FollowLogMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.FollowLogDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.Customer;
import com.hhzy.crm.modules.customer.entity.FollowLog;
import com.hhzy.crm.modules.customer.service.FollowLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/11 13:06
 * @Description:
 */
@Service
@Transactional
@Slf4j
public class FollowLogServiceImpl implements FollowLogService {

    @Autowired
    private FollowLogMapper followLogMapper;

    @Override
    public void save(FollowLog followLog) {
        followLog.setCreateTime(new Date());
        followLog.setUpdateTime(new Date());
        followLogMapper.insertSelective(followLog);
    }

    @Override
    public void update(FollowLog followLog) {
        followLog.setCreateTime(new Date());
        followLog.setUpdateTime(new Date());
        followLogMapper.updateByPrimaryKeySelective(followLog);
    }

    @Override
    public PageInfo<FollowLog> selcetByKeyWord(String keyWord,Long projectId,Long userId,Integer page,Integer pageSize) {
        if (page!=null&&pageSize!=null){
            PageHelper.startPage(page,pageSize);
        }
        List<FollowLog> followLogs = followLogMapper.selcetByKeyWord(keyWord,projectId,userId);
        return new PageInfo<>(followLogs);
    }

    @Override
    public FollowLog selectById(Long id) {
        FollowLog followLog = followLogMapper.selectByPrimaryKey(id);
        if (followLog==null){
            throw  new BusinessException("跟进记录不存在");
        }
        return followLog;
    }

    @Override
    public void deleteBatch(List<Long> followLogIdList) {
        Example example = new Example(FollowLog.class);
        example.createCriteria().andIn("id",followLogIdList);
        followLogMapper.deleteByExample(example);
    }

    @Override
    public void updateUser(Long id, Long userId) {
        FollowLog followLog = this.selectById(id);
        followLog.setUserId(userId);
        followLogMapper.updateByPrimaryKey(followLog);
    }

    @Override
    public FollowLog selectByMobile(Long projectId,String mobile) {
        Example example = new Example(FollowLog.class);
        example.createCriteria().andEqualTo("mobile",mobile).andEqualTo("projectId",projectId);
        List<FollowLog> followLogs = followLogMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(followLogs)){
            return followLogs.get(0);
        }
        return  null;
    }

    @Override
    public PageInfo<FollowLog> select(FollowLogDTO followLogDTO) {
        if (followLogDTO.getPage()!=null&&followLogDTO.getPageSize()!=null){
            PageHelper.startPage(followLogDTO.getPage(), followLogDTO.getPageSize());
        }
        List<FollowLog> select = followLogMapper.selectFollowLog(followLogDTO);
        return new PageInfo<>(select);
    }

    @Override
    public void removeUserId(List<Long> ids) {
        followLogMapper.removeUserId(ids);
    }

    @Override
    public void updateUserBatch(UserBatchDTO userBatchDTO) {
        if (CollectionUtils.isEmpty(userBatchDTO.getIds())){
            return;
        }
        if (userBatchDTO.getUserId()==null){
            throw new BusinessException("请选择置业顾问");
        }
        Example example = new Example(FollowLog.class);
        example.createCriteria().andIn("id",userBatchDTO.getIds());
        FollowLog followLog = new FollowLog();
        followLog.setUserId(userBatchDTO.getUserId());
        followLogMapper.updateByExampleSelective(followLog, example);
    }
}
