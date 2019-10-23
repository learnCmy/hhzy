package com.hhzy.crm.modules.customer.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.hhzy.crm.common.utils.DateUtils;
import com.hhzy.crm.modules.customer.dao.*;
import com.hhzy.crm.modules.customer.dataobject.dto.ShowDTO;
import com.hhzy.crm.modules.customer.dataobject.vo.LineChartData;
import com.hhzy.crm.modules.customer.dataobject.vo.Top10CusomterVO;
import com.hhzy.crm.modules.customer.dataobject.vo.Top10OfferBuyVo;
import com.hhzy.crm.modules.customer.entity.*;
import com.hhzy.crm.modules.customer.service.ShowService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther: cmy
 * @Date: 2019/9/24 22:19
 * @Description:
 */
@Service
public class ShowServiceImpl implements ShowService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private SignInfoMapper signInfoMapper;

    @Autowired
    private CallLogMapper callLogMapper;

    @Autowired
    private FollowLogMapper followLogMapper;

    @Autowired
    private OfferBuyMapper offerBuyMapper;

    @Override
    public Map<String,Object> count(ShowDTO showDTO){
        //定义返回对象
        HashMap<String, Object> map = Maps.newHashMap();
        handleDate(showDTO);
        Date beginDate = showDTO.getStartTime();
        Date endDate = showDTO.getEndTime();
        Long projectId = showDTO.getProjectId();
        //统计来访
        Example example = new Example(Customer.class);
        Example.Criteria criteria = example.createCriteria().
                andEqualTo("projectId", projectId);
        if (beginDate!=null&&endDate!=null){
            criteria.andBetween("comingTime",beginDate,endDate);
        }
        int i = customerMapper.selectCountByExample(example);
        map.put("customer",i);

        //统计来电
        Example callexample = new Example(CallLog.class);
        Example.Criteria callCriteria = callexample.createCriteria().andEqualTo("projectId", projectId);
        if (beginDate!=null&&endDate!=null){
            callCriteria.andBetween("callTime",beginDate,endDate);
        }
        int callCount = callLogMapper.selectCountByExample(callexample);
        map.put("call",callCount);


        //统计回访
        Example followexample = new Example(FollowLog.class);
        Example.Criteria followCriteria = followexample.createCriteria().andEqualTo("projectId", projectId);
        if (beginDate!=null&&endDate!=null){
            followCriteria.andBetween("nextVisitTime",beginDate,endDate);
        }
        int followCount = followLogMapper.selectCountByExample(followexample);
        map.put("follow",followCount);


        //统计签约
        Example signExample = new Example(SignInfo.class);
        Example.Criteria signCriteria = signExample.createCriteria().andEqualTo("projectId", projectId);
        if (beginDate!=null&&endDate!=null){
            signCriteria.andBetween("signTime",beginDate,endDate);
        }
        int singCount = signInfoMapper.selectCountByExample(signExample);
        map.put("sign",singCount);



        //统计认购
        Example offerExample = new Example(OfferBuy.class);
        Example.Criteria offerCriteria = offerExample.createCriteria().andEqualTo("projectId", projectId);
        if (beginDate!=null&&endDate!=null){
            offerCriteria.andBetween("offerBuyTime",beginDate,endDate);
        }
        int offerCount = offerBuyMapper.selectCountByExample(offerExample);
        map.put("offer",offerCount);
        return map;
    }

    @Override
    public Map<String, Object> coutSell(Long projectId) {
        Date date = new Date();
        HashMap<String, Object> map = Maps.newHashMap();
        BigDecimal bigDecimal = signInfoMapper.sellAmountCount(null, null,projectId);
        map.put("totalAmount",bigDecimal);

        Date[] weekStartAndEnd = DateUtils.getWeekStartAndEnd(0);
        Date beginDate = weekStartAndEnd[0];
        Date endDate =weekStartAndEnd[1];
        BigDecimal weekAmount = signInfoMapper.sellAmountCount(beginDate, endDate,projectId);
        map.put("weekAmount",weekAmount);

        BigDecimal monthAmount = signInfoMapper.sellAmountCount( DateUtils.getDateFirstDay(date),DateUtils.getDateLastDay(date),projectId);
        map.put("monthAmount",monthAmount);
        return map;
    }

    @Override
    public List<Top10CusomterVO> listTop10Customer(Long projectId) {
        List<Top10CusomterVO> top10CusomterVOS = customerMapper.selectTop10Cusomter(projectId);
        return top10CusomterVOS;
    }

    public List<Top10OfferBuyVo> listTop10OfferBuy(Long projectId){
        List<Top10OfferBuyVo> top10OfferBuyVos = offerBuyMapper.selectTop10(projectId);
        return top10OfferBuyVos;
    }




    private void filter(Example example,Long projectId,Date beginDate,Date endDate){
        Example.Criteria criteria = example.createCriteria().
                andEqualTo("projectId", projectId);
        if (beginDate!=null&&endDate!=null){
            criteria.andBetween("createTime",beginDate,endDate);
        }
    }


    private void handleDate(ShowDTO showDTO){
        Date date = new Date();
        Date beginDate=null;
        Date endDate=null;
        String dateType = showDTO.getDateType();
        if (StringUtils.isNotBlank(dateType)){
            switch (dateType){
                case "date":
                    beginDate = DateUtils.getBeginDate(date);
                    endDate = DateUtils.getEndDate(date);
                    showDTO.setStartTime(beginDate);
                    showDTO.setEndTime(endDate);
                    break;
                case "week":
                    Date[] weekStartAndEnd = DateUtils.getWeekStartAndEnd(0);
                    beginDate = weekStartAndEnd[0];
                    endDate =weekStartAndEnd[1];
                    showDTO.setStartTime(beginDate);
                    showDTO.setEndTime(endDate);
                    break;
                case "month":
                    DateUtils.getDateFirstDay(date);
                    DateUtils.getDateLastDay(date);
                    showDTO.setStartTime(beginDate);
                    showDTO.setEndTime(endDate);
                    break;
            }
        }
    }


    public Map<String,Object> lineChart(Long projectId){
        HashMap<String, Object> map = Maps.newHashMap();
        List<LineChartData> customerDataChartList = Lists.newArrayList();
        List<LineChartData> callDataChartList = Lists.newArrayList();
        List<LineChartData> followDataChartList = Lists.newArrayList();
        List<LineChartData> signDataChartList= Lists.newArrayList();
        List<LineChartData> offerDataChartList=Lists.newArrayList();
        //统计来访
        for (int i = 7; i >=0 ; i--) {
            DateTime dateTime = new DateTime();
            Date date = dateTime.minusDays(i).toDate();
            Date beginDate = DateUtils.getBeginDate(date);
            Date endDate = DateUtils.getEndDate(date);
            Example example = new Example(Customer.class);
            Example.Criteria criteria = example.createCriteria().
                    andEqualTo("projectId", projectId);
            if (beginDate!=null&&endDate!=null){
                criteria.andBetween("comingTime",beginDate,endDate);
            }
            int customerCount = customerMapper.selectCountByExample(example);
            LineChartData customerDataChart = new LineChartData();
            customerDataChart.setX(DateUtils.format(date));
            customerDataChart.setY(customerCount);
            customerDataChartList.add(customerDataChart);
            //统计来电
            Example callexample = new Example(CallLog.class);
            Example.Criteria callCriteria = callexample.createCriteria().andEqualTo("projectId", projectId);
            if (beginDate!=null&&endDate!=null){
                callCriteria.andBetween("callTime",beginDate,endDate);
            }
            int callCount = callLogMapper.selectCountByExample(callexample);
            LineChartData callDataChart = new LineChartData();
            callDataChart.setX(DateUtils.format(date));
            callDataChart.setY(callCount);
            callDataChartList.add(callDataChart);

            //统计回访
            Example followexample = new Example(FollowLog.class);
            Example.Criteria followCriteria = followexample.createCriteria().andEqualTo("projectId", projectId);
            if (beginDate!=null&&endDate!=null){
                followCriteria.andBetween("nextVisitTime",beginDate,endDate);
            }
            int followCount = followLogMapper.selectCountByExample(followexample);
            LineChartData followDataChart = new LineChartData();
            followDataChart.setX(DateUtils.format(date));
            followDataChart.setY(followCount);
            followDataChartList.add(followDataChart);

            //统计签约
            Example signExample = new Example(SignInfo.class);
            Example.Criteria signCriteria = signExample.createCriteria().andEqualTo("projectId", projectId);
            if (beginDate!=null&&endDate!=null){
                signCriteria.andBetween("signTime",beginDate,endDate);
            }
            int singCount = signInfoMapper.selectCountByExample(signExample);
            LineChartData signDataChart = new LineChartData();
            signDataChart.setX(DateUtils.format(date));
            signDataChart.setY(singCount);
            signDataChartList.add(signDataChart);

            //统计认购
            Example offerExample = new Example(OfferBuy.class);
            Example.Criteria offerCriteria = offerExample.createCriteria().andEqualTo("projectId", projectId);
            if (beginDate!=null&&endDate!=null){
                offerCriteria.andBetween("offerBuyTime",beginDate,endDate);
            }
            int offerCount = offerBuyMapper.selectCountByExample(offerExample);
            LineChartData offerDataChart = new LineChartData();
            offerDataChart.setX(DateUtils.format(date));
            offerDataChart.setY(offerCount);
            offerDataChartList.add(offerDataChart);
        }
        map.put("customerDataChartList",customerDataChartList);
        map.put("callDataChartList",callDataChartList);
        map.put("followDataChartList",followDataChartList);
        map.put("signDataChartList",signDataChartList);
        map.put("offerDataChartList",offerDataChartList);
        return map;
    }

}
