package com.sangjie.workbench.dao;

import com.sangjie.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    List<Activity> getActivityList(Map map);

    int getTotalActivity(Map map);


    int deleteActivcity(String[] idList);

    int updateActivity(Activity activity);

    Activity getActivityById(String id);

    Activity showDetail(String id);
}
