package com.hhzy.crm.modules.customer.service;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseService;
import com.hhzy.crm.modules.customer.dataobject.dto.SignDTO;
import com.hhzy.crm.modules.customer.dataobject.export.SignExportResult;
import com.hhzy.crm.modules.customer.dataobject.vo.SignVo;
import com.hhzy.crm.modules.customer.entity.SignInfo;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/24 10:16
 * @Description:
 */
public interface SignInfoService  extends BaseService<SignInfo> {

    void  deleteBatch(List<Long> ids);

    void updateOrSaveAnjie(SignInfo signInfo);

    PageInfo<SignInfo> selectList(SignDTO signDTO);

    PageInfo<SignVo> selectSignVo(SignDTO signDTO);

}
