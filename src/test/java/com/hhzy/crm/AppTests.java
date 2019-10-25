/*
package com.hhzy.crm;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hhzy.crm.modules.customer.dataobject.dto.TookeenDTO;
import com.hhzy.crm.modules.customer.entity.Tookeen;
import com.hhzy.crm.modules.customer.service.TookeenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * @Auther: cmy
 * @Date: 2019/10/23 14:48
 * @Description:
 *//*

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTests {

    @Autowired
    private TookeenService tookeenService;

    @Test
    public  void save(){
        Tookeen tookeen = new Tookeen();
        tookeen.setUserId(33L);
        tookeen.setMobile("15200758");
        tookeen.setProjectId(8L);
        tookeenService.saveBasicTookeen(tookeen);

    }

    @Test
    public  void update(){
        Tookeen tookeen = new Tookeen();
        tookeen.setId(1L);
        tookeen.setName("张三");
        tookeen.setUserId(33L);
        tookeen.setMobile("134");
        tookeen.setProjectId(8L);
        tookeenService.updateBasicTookeen(tookeen);

    }

    @Test
    public  void list(){
        TookeenDTO tookeenDTO = new TookeenDTO();
        tookeenDTO.setUserId(44l);
        tookeenDTO.setProjectId(8L);
        tookeenDTO.setSortClause("create_time");
        List<Tookeen> tookeens = tookeenService.selectList(tookeenDTO);
        System.out.println(JSON.toJSONString(tookeens,true));

    }

    @Test
    public void remove(){
        ArrayList<Long> longs = Lists.newArrayList(1l);
        tookeenService.removeUserId(longs);
    }

    @Test
    public  void updateUser(){
        tookeenService.updateUser(1l,44l);
    }

}
*/
