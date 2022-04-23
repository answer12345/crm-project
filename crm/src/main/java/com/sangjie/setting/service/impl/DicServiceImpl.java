package com.sangjie.setting.service.impl;

import com.sangjie.setting.dao.DicTypeDao;
import com.sangjie.setting.dao.DicValueDao;
import com.sangjie.setting.domain.DicValue;
import com.sangjie.setting.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    @Resource
    private DicTypeDao dicTypeDao;

    @Resource
    private DicValueDao dicValueDao;

    @Override
    public Map<String,  List<DicValue>> getAll(ServletContext application) {
        //DicTypeDao dicTypeDao = WebApplicationContextUtils.getWebApplicationContext(application).getBean(DicTypeDao.class);
        //DicValueDao dicValueDao = WebApplicationContextUtils.getWebApplicationContext(application).getBean(DicValueDao.class);

        Map<String, List<DicValue>> map = new HashMap<>();

        List<String> codeList = dicTypeDao.getCode();
        for (String code : codeList) {
            List<DicValue> value = dicValueDao.getValueByCode(code);
            map.put(code, value);
        }
        return map;
    }
}
