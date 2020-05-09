package com.hhzy.crm.modules.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseServiceImpl;
import com.hhzy.crm.common.enums.HouseStatusEnum;
import com.hhzy.crm.common.enums.OfferBuyStatusEnum;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.modules.customer.dao.HouseMapper;
import com.hhzy.crm.modules.customer.dao.SignInfoMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.SignDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.SignInfoDTO;
import com.hhzy.crm.modules.customer.dataobject.export.SignExportResult;
import com.hhzy.crm.modules.customer.dataobject.vo.SignVo;
import com.hhzy.crm.modules.customer.entity.House;
import com.hhzy.crm.modules.customer.entity.OfferBuy;
import com.hhzy.crm.modules.customer.entity.SignInfo;
import com.hhzy.crm.modules.customer.service.HouseService;
import com.hhzy.crm.modules.customer.service.OfferBuyService;
import com.hhzy.crm.modules.customer.service.SignInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.mockito.internal.matchers.And;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: cmy
 * @Date: 2019/8/24 10:23
 * @Description:
 */
@Service
@Slf4j
@Transactional
public class SignInfoServiceImpl extends BaseServiceImpl<SignInfo> implements SignInfoService {

    @Autowired
    private SignInfoMapper signInfoMapper;

    @Autowired
    private OfferBuyService offerBuyService;

    @Autowired
    private HouseMapper houseMapper;


    @Override
    public void deleteBatch(List<Long> ids) {
        Example example = new Example(SignInfo.class);
        example.createCriteria().andIn("id",ids);
        signInfoMapper.deleteByExample(example);
    }

    @Override
    public void updateOrSaveAnjie(SignInfoDTO signInfoDTO) {
        Long offerBuyId = signInfoDTO.getOfferBuyId();
        OfferBuy buy = offerBuyService.selectById(offerBuyId);
        if (buy==null){
            throw  new BusinessException("认购者信息不存在");
        }
        if (!OfferBuyStatusEnum.CHECKED.getCode().equals(buy.getStatus())){
            throw new BusinessException("此认购信息状态不正确");
        }
        Example example = new Example(SignInfo.class);
        example.createCriteria().andEqualTo("projectId",signInfoDTO.getProjectId())
                .andEqualTo("offerBuyId",signInfoDTO.getOfferBuyId());
        List<SignInfo> signInfos = signInfoMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(signInfos)){
            SignInfo signInfo = signInfos.get(0);
            BeanUtils.copyProperties(signInfoDTO,signInfo);
            signInfoMapper.updateByPrimaryKey(signInfo);
        }else {
            SignInfo signInfo = new SignInfo();
            BeanUtils.copyProperties(signInfoDTO,signInfo);
            signInfoMapper.insertSelective(signInfo);
        }
    }

    @Override
    public PageInfo<SignInfo> selectList(SignDTO signDTO) {
        if (signDTO.getPage()!=null&&signDTO.getPageSize()!=null){
            PageHelper.startPage(signDTO.getPage(),signDTO.getPageSize());
        }
        List<SignInfo> signInfos = signInfoMapper.selectList(signDTO);
        return new PageInfo<>(signInfos);
    }

    @Override
    public PageInfo<SignVo> selectSignVoExport(SignDTO signDTO) {
        if (signDTO.getPage()!=null&&signDTO.getPageSize()!=null){
            PageHelper.startPage(signDTO.getPage(),signDTO.getPageSize());
        }
        List<SignVo> signVos = signInfoMapper.selectSignVoExport(signDTO);
        return new PageInfo<>(signVos);
    }

    @Override
    public PageInfo<SignVo> selectSignVo(SignDTO signDTO) {
        if (signDTO.getPage()!=null&&signDTO.getPageSize()!=null){
            PageHelper.startPage(signDTO.getPage(),signDTO.getPageSize());
        }
        List<SignVo> signVos = signInfoMapper.selectSignVo(signDTO);
        return new PageInfo<>(signVos);
    }


}
