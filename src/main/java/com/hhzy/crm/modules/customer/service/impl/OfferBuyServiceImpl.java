package com.hhzy.crm.modules.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.hhzy.crm.common.base.BaseServiceImpl;
import com.hhzy.crm.common.enums.HouseStatusEnum;
import com.hhzy.crm.common.enums.OfferBuyStatusEnum;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.modules.customer.dao.HouseMapper;
import com.hhzy.crm.modules.customer.dao.OfferBuyMapper;
import com.hhzy.crm.modules.customer.dao.SignInfoMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.OfferBuyDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.House;
import com.hhzy.crm.modules.customer.entity.OfferBuy;
import com.hhzy.crm.modules.customer.entity.SignInfo;
import com.hhzy.crm.modules.customer.service.HouseService;
import com.hhzy.crm.modules.customer.service.IdentifyService;
import com.hhzy.crm.modules.customer.service.OfferBuyService;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/8/24 10:15
 * @Description:
 */
@Service
@Transactional
@Slf4j
public class OfferBuyServiceImpl extends BaseServiceImpl<OfferBuy> implements OfferBuyService {


    @Autowired
    private OfferBuyMapper offerBuyMapper;

    @Autowired
    private HouseMapper houseMapper;


    @Autowired
    private HouseService houseService;


    @Autowired
    private SignInfoMapper signInfoMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private IdentifyService identifyService;


    @Override
    public OfferBuy selectById(Long id) {
        OfferBuy offerBuy = offerBuyMapper.selectByPrimaryKey(id);
        if (offerBuy==null){
            throw new BusinessException("购买记录不存在");
        }
        House house = houseMapper.selectByPrimaryKey(offerBuy.getHouseId());
        if (house!=null){
            offerBuy.setHouseName(house.getName());
        }
        SysUser sysUser = sysUserService.selectSysUserInfo(offerBuy.getUserId());
        offerBuy.setUserName(sysUser.getUsername());
        return offerBuy;
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        Example example = new Example(OfferBuy.class);
        example.createCriteria().andIn("id",ids);
        offerBuyMapper.deleteByExample(example);
    }

    public void deleteBatchForWeb(List<Long> ids){
        Example example = new Example(SignInfo.class);
        example.createCriteria().andIn("offerBuyId",ids);
        List<SignInfo> signInfoList= signInfoMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(signInfoList)){
            throw new BusinessException("所选认购记录存在已经签约得信息无法删除，请先删除对应签约信息");
        }
        Example buyExample = new Example(OfferBuy.class);
        buyExample.createCriteria().andIn("id",ids);
        offerBuyMapper.deleteByExample(buyExample);
    }


    @Override
    public PageInfo<OfferBuy> selectByName(String keyWord,Long userId, Long projectId,Integer page, Integer pageSize) {
        if (page!=null&&pageSize!=null){
            PageHelper.startPage(page,pageSize);
        }
        List<OfferBuy> offerBuys = offerBuyMapper.selectByName(keyWord, userId, projectId);
        offerBuys.forEach(e->{
            House house = houseMapper.selectByPrimaryKey(e.getHouseId());
            if (house!=null){
                e.setHouseName(house.getName());
            }
        });
        return new PageInfo<>(offerBuys);
    }


    @Override
    public void updateOfferBuy(OfferBuy offerBuy) {
        OfferBuy buy = this.queryById(offerBuy.getId());
        if (buy==null){
            throw  new BusinessException("认购信息不存在");
        }
        if (OfferBuyStatusEnum.CHECKED.getCode().equals(buy.getStatus())){
            throw  new BusinessException("此认购信息已审核，不能更新");
        }
        this.updateOfferBuy(offerBuy);
    }

    @Override
    public void updatePrePrice(OfferBuy offerBuy) {
        OfferBuy buy = this.queryById(offerBuy.getId());
        if (OfferBuyStatusEnum.CHECKED.getCode().equals(buy.getStatus())){
            throw  new BusinessException("此认购信息 已确认 请 取消审核");
        }
        offerBuyMapper.updateByPrimaryKeySelective(offerBuy);
    }

    @Override
    public PageInfo<OfferBuy> selectList(OfferBuyDTO offerBuyDTO) {
        if (offerBuyDTO.getPage()!=null&&offerBuyDTO.getPageSize()!=null){
            PageHelper.startPage(offerBuyDTO.getPage(),offerBuyDTO.getPageSize());
        }
        List<OfferBuy> offerBuys = offerBuyMapper.selectList(offerBuyDTO);
        offerBuys.forEach(e->{
            Example example = new Example(SignInfo.class);
            example.createCriteria().andEqualTo("offerBuyId",e.getId()).andEqualTo("projectId",e.getProjectId());
            List<SignInfo> signInfoList= signInfoMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(signInfoList)){
                e.setSignId(signInfoList.get(0).getId());
            }
        });
        return new PageInfo<>(offerBuys);
    }

    @Override
    public void updateUser(Long id, Long userId) {
        OfferBuy buy = this.queryById(id);
        buy.setUserId(userId);
        offerBuyMapper.updateByPrimaryKeySelective(buy);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        OfferBuy buy = this.selectById(id);
        if (status.equals(OfferBuyStatusEnum.CHECKED.getCode())&&buy.getPrePrice()==null){
            throw  new BusinessException("请先填写定金，在审核通过");
        }
        buy.setStatus(status);
        this.updateSelective(buy);
        //更新房屋状态
        if (status.equals(OfferBuyStatusEnum.CHECKED.getCode())){
            House house = houseService.selectHouseInfo(buy.getHouseId());
            if (HouseStatusEnum.FINISH.getCode()==house.getStatus()){
                throw new BusinessException("此房屋已售卖,无法审核通过,请仔细核对");
            }
            houseService.updateStatus(buy.getHouseId(),buy.getId(),HouseStatusEnum.FINISH.getCode());

        }else {
            House house = houseService.selectHouseInfo(buy.getHouseId());
            if (HouseStatusEnum.FINISH.getCode()==house.getStatus()){
                houseService.updateStatus(buy.getHouseId(),null,HouseStatusEnum.SELLING.getCode());
            }
        }
    }

    @Override
    public void updateUserBatch(UserBatchDTO userBatchDTO) {
        if (CollectionUtils.isEmpty(userBatchDTO.getIds())){
            return;
        }
        if (userBatchDTO.getIds()==null){
            throw new BusinessException("请选择置业顾问");
        }
        Example example = new Example(OfferBuy.class);
        example.createCriteria().andIn("id",userBatchDTO.getIds());
        OfferBuy buy = new OfferBuy();
        buy.setUserId(userBatchDTO.getUserId());
        offerBuyMapper.updateByExampleSelective(buy,example);
    }

    @Override
    public void removeUserId(List<Long> ids) {
        offerBuyMapper.removeUserId(ids);
    }

    @Override
    public List<OfferBuy> selectByMobile(Long projectId, String mobile) {
        Example example = new Example(OfferBuy.class);
        example.createCriteria().andEqualTo("mobile",mobile)
                .andEqualTo("projectId",projectId);
        List<OfferBuy> offerBuys = offerBuyMapper.selectByExample(example);
        return offerBuys;
    }

    @Override
    public Map<String, Object> countNumberByStatus(Long projectId) {
        Map<String ,Object> map=Maps.newHashMap();
        int unCheckedCount = offerBuyMapper.countNumberByStatus(projectId, OfferBuyStatusEnum.UNCHECKED.getCode());
        int checkedCount = offerBuyMapper.countNumberByStatus(projectId, OfferBuyStatusEnum.CHECKED.getCode());
        int rejectCount = offerBuyMapper.countNumberByStatus(projectId, OfferBuyStatusEnum.REJECT.getCode());
        map.put("unCheckedNum",unCheckedCount);
        map.put("checkedNum",checkedCount);
        map.put("rejectNum",rejectCount);
        return map;
    }
}
