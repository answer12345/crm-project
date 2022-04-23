package com.sangjie.workbench.dao;

import com.sangjie.dto.ClueRemarkAndClueDto;
import com.sangjie.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {
    List<ClueRemarkAndClueDto> showRemark(String clueId);

    List<ClueRemark> query(String clueId);

    int deleteByClueId(String clueId);

    int delete(String clueRemarkId);
}
