package com.sangjie.workbench.service.impl;

import com.sangjie.workbench.dao.ActivityDao;
import com.sangjie.workbench.dao.ActivityRemarkDao;
import com.sangjie.workbench.domain.Activity;
import com.sangjie.workbench.domain.ActivityRemark;
import com.sangjie.workbench.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao activityDao;

    @Resource
    private ActivityRemarkDao activityRemarkDao;

    @Override
    public int save(Activity activity) {
        int res = activityDao.save(activity);
        return res;
    }

    @Override
    public List<Activity> getActivityList(Map map) {
        List<Activity> activityList = activityDao.getActivityList(map);
        return activityList;
    }

    @Override
    public int getTotalActivity(Map map) {
        int total = activityDao.getTotalActivity(map);
        return total;
    }

    @Override
    public boolean deleteList(String[] idList) {

        boolean flag = true;

        //删除activity同时删除activityRemark
        //查找需要删除的所有的activityRemark
        int num1 = activityRemarkDao.selectDeleteNums(idList);

        //实际删除的数量
        int num2 = activityRemarkDao.deleteNums(idList);

        if (num1 != num2) {
            flag = false;
        }

        //删除activity
        int num3 = activityDao.deleteActivcity(idList);

        if (num3 != idList.length) {
            flag = false;
        }

        return flag;
    }

    @Override
    public int updateActivity(Activity activity) {
        int res = activityDao.updateActivity(activity);
        return res;
    }

    @Override
    public Activity getActivityById(String id) {
        Activity a = activityDao.getActivityById(id);
        return a;
    }

    @Override
    public Activity showDetail(String id) {
        Activity activity = activityDao.showDetail(id);

        return activity;
    }

    @Override
    public List<ActivityRemark> showRemarkList(String aid) {
        return activityRemarkDao.showRemarkList(aid);
    }

    @Override
    public boolean removeRemark(String id) {
        boolean flag = true;

        int res = activityRemarkDao.removeRemark(id);

        if (res != 1) {
            flag = false;
        }
        return flag;
    }
}
