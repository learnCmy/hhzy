package com.hhzy.crm.modules.customer.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hhzy.crm.common.enums.HouseStatusEnum;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.modules.customer.dao.HouseMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.HouseDTO;
import com.hhzy.crm.modules.customer.dataobject.importPOI.HouseImport;
import com.hhzy.crm.modules.customer.entity.House;
import com.hhzy.crm.modules.customer.service.HouseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseMapper houseMapper;


    @Override
    public void save(House house) {
        Example example = new Example(House.class);
        example.createCriteria().andEqualTo("projectId",house.getProjectId()).andEqualTo("name",house.getName());
        List<House> houses = houseMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(houses)){
            throw new BusinessException("存在相同名字的房屋");
        }
        if (house.getStatus()==null){
            house.setStatus(HouseStatusEnum.SELLING.getCode());
        }
        houseMapper.insertSelective(house);
    }


    @Override
    public void update(House house){
        Example example = new Example(House.class);
        example.createCriteria().andEqualTo("projectId",house.getProjectId()).
                andEqualTo("name",house.getName()).andNotEqualTo("id",house.getId());
        List<House> houses = houseMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(houses)){
            throw new BusinessException("存在相同名字的房屋");
        }
        if (house.getStatus()==null){
            house.setStatus(HouseStatusEnum.SELLING.getCode());
        }
        houseMapper.updateByPrimaryKey(house);
    }

    @Override
    public PageInfo<House> selectHouse(HouseDTO houseDTO) {
        if (houseDTO.getPage()!=null&&houseDTO.getPageSize()!=null){
            PageHelper.startPage(houseDTO.getPage(),houseDTO.getPageSize());
        }
        List<House> houses = houseMapper.selectList(houseDTO);
        return new PageInfo<>(houses);
    }

    @Override
    public void updateStatus(Long houseId, Integer status) {
        House house = houseMapper.selectByPrimaryKey(houseId);
        if (house!=null){
            house.setStatus(status);
            houseMapper.updateByPrimaryKey(house);
        }
    }

    @Override
    public void updateStatus(Long houseId,Long offerbuyId, Integer status) {
        House house = houseMapper.selectByPrimaryKey(houseId);
        if (house!=null){
            house.setStatus(status);
            house.setOfferBuyId(offerbuyId);
            houseMapper.updateByPrimaryKey(house);
        }
    }


    @Override
    public House selectHouseInfo(Long houseId) {
        House house = houseMapper.selectByPrimaryKey(houseId);
        return house;
    }

    @Override
    public void updateHouseStatus(Long houseId,Integer status) {
        House house = new House();
        house.setId(houseId);
        house.setStatus(status);
        houseMapper.updateByPrimaryKeySelective(house);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        Example example = new Example(House.class);
        example.createCriteria().andIn("id",ids);
        houseMapper.deleteByExample(example);
    }

    @Override
    public Map<String, Object> importDatas(Long projectId, List<HouseImport> list,Integer type) {
        Map<String,Object> map=Maps.newHashMap();
        List<House> houseList=Lists.newArrayList();
        System.out.println(JSON.toJSONString(list.get(0)));
        for (HouseImport houseImport : list) {
            House house = new House();
            BeanUtils.copyProperties(houseImport,house);
            if (type.equals(1)){//住宅
                house.setName(houseImport.getBuildNo().concat(houseImport.getFloorLevel()).concat(houseImport.getRoomNo()));
            }else {
                house.setName(houseImport.getHouseInfo());
            }
            if (StringUtils.isEmpty(house.getName())){
                continue;
            }else {
                Example example = new Example(House.class);
                example.createCriteria().andEqualTo("projectId",projectId).andEqualTo("name",house.getName());
                List<House> houses = houseMapper.selectByExample(example);
                if (CollectionUtils.isNotEmpty(houses)){
                    continue;
                }else {
                    house.setProjectId(projectId);
                    house.setType(type);
                    house.setCreateTime(new Date());
                    house.setUpdateTime(new Date());
                    house.setStatus(HouseStatusEnum.SELLING.getCode());
                    houseList.add(house);
                }
            }
        }
        int i=0;
        if (CollectionUtils.isNotEmpty(houseList)){
            i = houseMapper.insertList(houseList);
        }
        map.put("insertNew",i);
        return map;
    }


}
