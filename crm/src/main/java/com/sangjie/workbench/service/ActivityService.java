package com.sangjie.workbench.service;

import com.sangjie.workbench.domain.Activity;
import com.sangjie.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    int save(Activity activity);

    List<Activity> getActivityList(Map map);

    int getTotalActivity(Map map);

    boolean deleteList(String[] idList);

    int updateActivity(Activity activity);

    Activity getActivityById(String id);

    Activity showDetail(String id);

    List<ActivityRemark> showRemarkList(String aid);

    boolean removeRemark(String id);
}
