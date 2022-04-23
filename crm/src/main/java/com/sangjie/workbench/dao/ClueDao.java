package com.sangjie.workbench.dao;

import com.sangjie.dto.PageListDto;
import com.sangjie.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {
    int addClue(Clue clue);

    List<Clue> pageList(Map<String, Object> map);

    Clue getClue(String id);

    Clue getClueById(String clueId);

    int delete(String clueId);
}
