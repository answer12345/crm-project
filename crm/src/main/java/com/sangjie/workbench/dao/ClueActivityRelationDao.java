package com.sangjie.workbench.dao;

import com.sangjie.workbench.domain.Activity;
import com.sangjie.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {
    List<Activity> showActivityList(String clueId);

    int delete(String id);

    List<ClueActivityRelation> getById(String clueId);

    List<Activity> getActivityListAndNoRelation(Map map);


    int bund(ClueActivityRelation clueActivityRelation);
}
