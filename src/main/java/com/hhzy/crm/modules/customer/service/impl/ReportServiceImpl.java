package com.hhzy.crm.modules.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseServiceImpl;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.common.utils.MobileUtils;
import com.hhzy.crm.modules.customer.dao.ReportMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.ReportDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.Report;
import com.hhzy.crm.modules.customer.entity.Tookeen;
import com.hhzy.crm.modules.customer.service.ReportService;
import com.hhzy.crm.modules.sys.service.SysUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2020/5/20 17:07
 * @Description:
 */
@Service
public class ReportServiceImpl  extends BaseServiceImpl<Report> implements ReportService {

    @Autowired
    private ReportMapper reportMapper;


    @Autowired
    private SysUserService sysUserService;

    @Override
    public void saveBasicReport(Report report) {
        String mobile = report.getMobile();
        Long projectId = report.getProjectId();
        mobile = MobileUtils.getMobile(mobile);
        report.setMobile(mobile);
        if(projectId== null){
            throw new BusinessException("项目Id不能为空,请联系管理员");
        }
        List<Report> reports = this.selectByMobile(projectId, mobile);
        if (CollectionUtils.isNotEmpty(reports)){
            String userName = sysUserService.getUserName(reports.get(0).getUserId());
            throw new BusinessException(String.format("客户已被【%s】录入",userName));
        }
        reportMapper.insertSelective(report);
    }

    @Override
    public void updateBasicReport(Report report) {
        String mobile = report.getMobile();
        Long projectId = report.getProjectId();
        if (StringUtils.isEmpty(mobile)){
            throw  new BusinessException("请填写手机号");
        }
        if(projectId== null){
            throw new BusinessException("项目Id不能为空,请联系管理员");
        }
        List<Report> reports = this.selectByMobile(projectId, mobile);
        if (CollectionUtils.isNotEmpty(reports)){
            if (!reports.get(0).getId().equals(report.getId())){
                String userName = sysUserService.getUserName(reports.get(0).getUserId());
                throw  new BusinessException(String.format("客户已被【%s】录入",userName));
            }
        }
        reportMapper.updateByPrimaryKeySelective(report);

    }

    @Override
    public List<Report> selectByMobile(Long projectId, String mobile){
        Example example = new Example(Report.class);
        example.createCriteria().andEqualTo("projectId",projectId)
                .andEqualTo("mobile",mobile);
        List<Report> reports = reportMapper.selectByExample(example);
        return reports;
    }

    @Override
    public PageInfo<Report> selectList(ReportDTO reportDTO) {
        if(reportDTO.getPage()!=null&&reportDTO.getPageSize()!=null){
            PageHelper.startPage(reportDTO.getPage(),reportDTO.getPageSize());
        }
        List<Report> reports = reportMapper.selectList(reportDTO);
        return new PageInfo<>(reports);
    }

    @Override
    public void removeUserId(List<Long> ids) {
        reportMapper.removeUserId(ids);
    }

    @Override
    public void updateUser(Long id, Long userId) {
        Report report = this.queryById(id);
        report.setUserId(userId);
        reportMapper.updateByPrimaryKey(report);
    }

    @Override
    public void updateUserBatch(UserBatchDTO userBatchDTO) {
        if (CollectionUtils.isEmpty(userBatchDTO.getIds())){
            return;
        }
        if (userBatchDTO.getUserId()==null){
            throw new BusinessException("请选择置业顾问");
        }
        Example example = new Example(Report.class);
        example.createCriteria().andIn("id",userBatchDTO.getIds());
        Report report = new Report();
        report.setUserId(userBatchDTO.getUserId());
        reportMapper.updateByExampleSelective(report,example);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        Example example = new Example(Report.class);
        example.createCriteria().andIn("id",ids);
        reportMapper.deleteByExample(example);
    }
}
