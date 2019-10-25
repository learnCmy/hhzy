package com.hhzy.crm.modules.customer.service.impl;

import cn.afterturn.easypoi.excel.ExcelXorHtmlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseServiceImpl;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.modules.customer.dao.TookeenMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.TookeenDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.*;
import com.hhzy.crm.modules.customer.service.*;
import com.hhzy.crm.modules.sys.service.SysUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/10/21 17:43
 * @Description:
 */
@Service
@Transactional
public class TookeenServiceImpl extends BaseServiceImpl<Tookeen> implements TookeenService {

    @Autowired
    private TookeenMapper tookeenMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CallLogService callLogService;

    @Autowired
    private OfferBuyService offerBuyService;

    @Autowired
    private IdentifyService identifyService;

    @Override
    public void saveBasicTookeen(Tookeen tookeen) {
        String mobile = tookeen.getMobile();
        Long projectId = tookeen.getProjectId();
        if (StringUtils.isEmpty(mobile)){
            throw  new BusinessException("请填写手机号");
        }
        if(projectId== null){
            throw new BusinessException("项目Id不能为空,请联系管理员");
        }
        List<Tookeen> tookeens = this.selectByMobile(projectId, mobile);
        if (CollectionUtils.isNotEmpty(tookeens)){
            String userName = sysUserService.getUserName(tookeens.get(0).getUserId());
            throw  new BusinessException(String.format("客户已被【%s】录入",userName));
        }
        this.checkSave(projectId,mobile);
        tookeenMapper.insertSelective(tookeen);
    }

    @Override
    public void updateBasicTookeen(Tookeen tookeen) {
        String mobile = tookeen.getMobile();
        Long projectId = tookeen.getProjectId();
        if (StringUtils.isEmpty(mobile)){
            throw  new BusinessException("请填写手机号");
        }
        if(projectId== null){
            throw new BusinessException("项目Id不能为空,请联系管理员");
        }
        List<Tookeen> tookeens = this.selectByMobile(projectId, mobile);
        if (CollectionUtils.isNotEmpty(tookeens)){
            if (!tookeens.get(0).getId().equals(tookeen.getId())){
                String userName = sysUserService.getUserName(tookeens.get(0).getUserId());
                throw  new BusinessException(String.format("客户已被【%s】录入",userName));
            }
        }
        this.checkSave(projectId,mobile);
        tookeenMapper.updateByPrimaryKeySelective(tookeen);
    }

    @Override
    public List<Tookeen> selectByMobile(Long projectId, String mobile) {
        Example example = new Example(Tookeen.class);
        example.createCriteria().andEqualTo("projectId",projectId)
                .andEqualTo("mobile",mobile);
        List<Tookeen> tookeens = tookeenMapper.selectByExample(example);
        return tookeens;
    }

    @Override
    public PageInfo<Tookeen> selectList(TookeenDTO tookeenDTO) {
        if(tookeenDTO.getPage()!=null&&tookeenDTO.getPageSize()!=null){
            PageHelper.startPage(tookeenDTO.getPage(),tookeenDTO.getPageSize());
        }
        List<Tookeen> tookeens = tookeenMapper.selectList(tookeenDTO);
        return new PageInfo<>(tookeens);
    }

    @Override
    public void removeUserId(List<Long> ids) {
        tookeenMapper.removeUserId(ids);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        Example example = new Example(Tookeen.class);
        example.createCriteria().andIn("id",ids);
        tookeenMapper.deleteByExample(example);
    }

    @Override
    public void updateUser(Long id, Long userId) {
        Tookeen tookeen = this.queryById(id);
        tookeen.setUserId(userId);
        tookeenMapper.updateByPrimaryKey(tookeen);
    }

    @Override
    public void updateUserBatch(UserBatchDTO userBatchDTO) {
        if (CollectionUtils.isEmpty(userBatchDTO.getIds())){
            return;
        }
        if (userBatchDTO.getUserId()==null){
            throw new BusinessException("请选择置业顾问");
        }
        Example example = new Example(Tookeen.class);
        example.createCriteria().andIn("id",userBatchDTO.getIds());
        Tookeen tookeen = new Tookeen();
        tookeen.setUserId(userBatchDTO.getUserId());
        tookeenMapper.updateByExampleSelective(tookeen,example);
    }


    private void checkSave(Long projectId,String mobile){

        List<Customer> list = customerService.selectCustomerByMobile(mobile, projectId);
        if (CollectionUtils.isNotEmpty(list)){
            String userName = sysUserService.getUserName(list.get(0).getUserId());
            throw  new BusinessException(String.format("客户为来访客户,已被【%s】录入",userName));
        }

        List<CallLog> callLogs = callLogService.selectByMobile(projectId, mobile);
        if (CollectionUtils.isNotEmpty(callLogs)){
            String userName = sysUserService.getUserName(callLogs.get(0).getUserId());
            throw  new BusinessException(String.format("客户为来电客户,已被【%s】录入",userName));
        }

        List<IdentifyLog> identifyLogs = identifyService.selectByMobile(projectId, mobile);
        if (CollectionUtils.isNotEmpty(identifyLogs)){
            String userName = sysUserService.getUserName(identifyLogs.get(0).getUserId());
            throw  new BusinessException(String.format("客户为认筹客户,已被【%s】录入",userName));
        }
        List<OfferBuy> offerBuys = offerBuyService.selectByMobile(projectId, mobile);
        if (CollectionUtils.isNotEmpty(offerBuys)){
            String userName = sysUserService.getUserName(offerBuys.get(0).getUserId());
            throw  new BusinessException(String.format("客户为认购客户,已被【%s】录入",userName));
        }

    }
}
