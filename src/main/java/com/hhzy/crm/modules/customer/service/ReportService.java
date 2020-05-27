package com.hhzy.crm.modules.customer.service;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseService;
import com.hhzy.crm.modules.customer.dataobject.dto.ReportDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.Report;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2020/5/20 17:06
 * @Description:
 */
public interface ReportService extends BaseService<Report> {

    void saveBasicReport(Report report);

    void  updateBasicReport(Report report);

    public List<Report> selectByMobile(Long projectId, String mobile);


    PageInfo<Report> selectList(ReportDTO reportDTO);

    void removeUserId(List<Long> ids);

    void updateUser(Long id,Long userId);

    void updateUserBatch(UserBatchDTO userBatchDTO);

    void deleteBatch(List<Long> ids);
}
