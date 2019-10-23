package com.hhzy.crm.modules.customer.service;


import com.github.pagehelper.PageInfo;
import com.hhzy.crm.modules.customer.dataobject.dto.HouseDTO;
import com.hhzy.crm.modules.customer.dataobject.importPOI.HouseImport;
import com.hhzy.crm.modules.customer.entity.House;

import java.util.List;
import java.util.Map;

public interface HouseService {

    void  save(House house);

    void update(House house);

    PageInfo<House> selectHouse(HouseDTO houseDTO);

    void updateStatus(Long houseId,Integer status);

    void updateStatus(Long houseId,Long offerId,Integer status);

    House selectHouseInfo(Long houseId);

    void updateHouseStatus(Long houseId,Integer status);

   void  deleteBatch(List<Long> ids);

    Map<String,Object>  importDatas(Long projectId, List<HouseImport> list,Integer type);

}
