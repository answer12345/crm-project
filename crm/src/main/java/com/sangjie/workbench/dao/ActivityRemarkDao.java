package com.sangjie.workbench.dao;

import com.sangjie.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {

    int selectDeleteNums(String[] idList);

    int deleteNums(String[] idList);

    List<ActivityRemark> showRemarkList(String aid);

    int removeRemark(String id);
}
