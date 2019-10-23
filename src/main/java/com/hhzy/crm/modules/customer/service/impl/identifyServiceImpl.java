package com.hhzy.crm.modules.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hhzy.crm.common.base.BaseServiceImpl;
import com.hhzy.crm.common.enums.IdentifySellStatusEnum;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.common.utils.EnumUtil;
import com.hhzy.crm.modules.customer.dao.CustomerMapper;
import com.hhzy.crm.modules.customer.dao.IdentifyLogMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.IdentifyDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.dataobject.importPOI.CustomerImport;
import com.hhzy.crm.modules.customer.dataobject.importPOI.IdentifyImport;
import com.hhzy.crm.modules.customer.entity.Customer;
import com.hhzy.crm.modules.customer.entity.IdentifyLog;
import com.hhzy.crm.modules.customer.service.IdentifyService;
import com.hhzy.crm.modules.sys.dao.SysUserDao;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.rmi.MarshalledObject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/8/24 10:05
 * @Description:
 */
@Service
@Transactional
@Slf4j
public class identifyServiceImpl extends BaseServiceImpl<IdentifyLog> implements IdentifyService {

    @Autowired
    private IdentifyLogMapper identifyLogMapper;

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public PageInfo<IdentifyLog> selcetByKeyWord(String keyWord,Long projectId,Long userId,Integer page,Integer pageSize) {
        if (page!=null&&pageSize!=null){
            PageHelper.startPage(page,pageSize);
        }
        List<IdentifyLog> identifyLogs = identifyLogMapper.selcetByKeyWord(keyWord,projectId,userId);
        return new PageInfo<>(identifyLogs);
    }

    @Override
    public IdentifyLog selectById(Long id) {
        IdentifyLog identifyLog = identifyLogMapper.selectByPrimaryKey(id);
        if (identifyLog==null){
            throw new BusinessException("所需来电记录不存在");
        }
        return identifyLog;
    }

    @Override
    public void deleteBatch(List<Long> identifyIdList) {
        Example example = new Example(IdentifyLog.class);
        example.createCriteria().andIn("id",identifyIdList);
        identifyLogMapper.deleteByExample(example);
    }

    @Override
    public PageInfo<IdentifyLog> selectList(IdentifyDTO identifyDTO) {
        if (identifyDTO.getPage()!=null&&identifyDTO.getPageSize()!=null){
            PageHelper.startPage(identifyDTO.getPage(),identifyDTO.getPageSize());
        }
        List<IdentifyLog> identifyLogs = identifyLogMapper.selectList(identifyDTO);
        return new PageInfo<>(identifyLogs);
    }

    @Override
    public void updateUser(Long id, Long UserId) {
        IdentifyLog identifyLog = this.queryById(id);
        if (identifyLog==null){
            throw  new BusinessException("此认筹信息不存在");
        }
        identifyLog.setUserId(UserId);
        identifyLogMapper.updateByPrimaryKey(identifyLog);
    }

    @Override
    public void updateUserBatch(UserBatchDTO userBatchDTO) {
        if (CollectionUtils.isEmpty(userBatchDTO.getIds())){
            return;
        }
        if (userBatchDTO.getIds()==null){
            throw new BusinessException("请选择置业顾问");
        }
        Example example = new Example(IdentifyLog.class);
        example.createCriteria().andIn("id",userBatchDTO.getIds());
        IdentifyLog identifyLog = new IdentifyLog();
        identifyLog.setUserId(userBatchDTO.getUserId());
        identifyLogMapper.updateByExampleSelective(identifyLog,example);
    }

    @Override
    public void removeUserId(List<Long> ids) {
        identifyLogMapper.removeUserId(ids);
    }

    @Override
    public void updateSellStatus(Long id, Integer sellStatus) {
        IdentifyLog identifyLog = identifyLogMapper.selectByPrimaryKey(id);
        if (identifyLog==null){
            throw new BusinessException("认筹记录不存在");
        }
        identifyLog.setSellStatus(sellStatus);
        if (!IdentifySellStatusEnum.REFUNDCARD.getCode().equals(sellStatus)) {
            identifyLog.setRefundTime(null);
        }
        identifyLogMapper.updateByPrimaryKey(identifyLog);
    }

    @Override
    public IdentifyLog selectByMobile(Long projectId,String mobile){
        Example example = new Example(IdentifyLog.class);
        example.createCriteria().andEqualTo("mobile",mobile).andEqualTo("projectId",projectId);
        List<IdentifyLog> identifyLogs = identifyLogMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(identifyLogs)){
            return identifyLogs.get(0);
        }else {
            return null;
        }
    }

    public Map<String,Object> importDatas(List<IdentifyImport> list,Long projectId){
        HashMap<String, Object> map = Maps.newHashMap();
        List<IdentifyLog> identifyArrayList = Lists.newArrayList();//需要新增的集合
        List<IdentifyImport> repeatIdentifyList=Lists.newArrayList();//重复的集合
        List<SysUser> sysUsers = sysUserDao.selectAll();//查出所有的用户
        HashMap<String, Object> sysUsersMap = Maps.newHashMap();
        sysUsers.forEach(e->{
            sysUsersMap.put(e.getUsername(),e.getUserId());
        });
        List<String> mobileByProject = identifyLogMapper.selectMobile(projectId);
        Date date = new Date();
        for (IdentifyImport identifyImport : list) {
            String mobile = identifyImport.getMobile();
            if (StringUtils.isEmpty(mobile)||mobile.length()>20){
                continue;
            }
            if (mobileByProject.contains(mobile)){
                repeatIdentifyList.add(identifyImport);
            }else {
                IdentifyLog identifyLog = new IdentifyLog();
                BeanUtils.copyProperties(identifyImport,identifyLog);
                if (StringUtils.isNotEmpty(identifyImport.getSellStatusStr())){
                    IdentifySellStatusEnum identifySellStatusEnum = EnumUtil.getByMessage(identifyImport.getSellStatusStr(), IdentifySellStatusEnum.class);
                    identifyLog.setSellStatus(identifySellStatusEnum==null?IdentifySellStatusEnum.BUYCARD.getCode():identifySellStatusEnum.getCode());
                }
                if (StringUtils.isNotEmpty(identifyImport.getSourceWayStr())){
                    IdentifySellStatusEnum enums= EnumUtil.getByMessage(identifyImport.getSourceWayStr(), IdentifySellStatusEnum.class);
                    identifyLog.setSourceWay(enums==null?null:enums.getCode());
                }
                identifyLog.setProjectId(projectId);
                identifyLog.setCreateTime(date);
                identifyLog.setUpdateTime(date);
                Object object = sysUsersMap.get(identifyLog.getUserName());
                if (object!=null){
                    identifyLog.setUserId((Long)object);
                }
                identifyArrayList.add(identifyLog);
            }
        }
        int insertNew=0;
        if (CollectionUtils.isNotEmpty(identifyArrayList)){
            insertNew= identifyLogMapper.insertList(identifyArrayList);
        }
        map.put("insertNew",insertNew);
        map.put("repeat",repeatIdentifyList.size());
        return map;
    }

    @Override
    public void updateByPrimaryKeySelective(IdentifyLog identifyLog){
        identifyLogMapper.updateByPrimaryKeySelective(identifyLog);
    }


}
