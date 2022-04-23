package com.sangjie.workbench.dao;

import com.sangjie.workbench.domain.Tran;


public interface TranDao {

    int save(Tran tran);

    Tran getTranById(String id);

    int changeStage(Tran tran);
}
