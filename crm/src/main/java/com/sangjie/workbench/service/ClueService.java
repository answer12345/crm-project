package com.sangjie.workbench.service;

import com.sangjie.dto.ClueRemarkAndClueDto;
import com.sangjie.dto.PageListDto;
import com.sangjie.exception.ConvertException;
import com.sangjie.setting.domain.User;
import com.sangjie.workbench.domain.Activity;
import com.sangjie.workbench.domain.Clue;
import com.sangjie.workbench.domain.Tran;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface ClueService {

    List<User> selectUserList();

    boolean addClue(Clue clue);

    List<Clue> pageList(Map<String, Object> map);

    Clue getClue(String id);

    boolean convert(String clueId, Tran tran, String createBy) throws ConvertException;

    List<ClueRemarkAndClueDto> showRemark(String clueId);

    List<Activity> showActivityList(String clueId);

    int deleteRelation(String id);

    List<Activity> getActivityListAndNoRelation(Map map);

    boolean bund(String cid, String[] aids);
}
