package com.sangjie.workbench.service;

import com.sangjie.exception.CreateException;
import com.sangjie.workbench.domain.Tran;

import java.util.List;

public interface TranService {
    Tran getTran(String id);

    boolean changeStage(Tran tran);

    List<String> getCustomerName();

    boolean save(Tran tran, String customerName) throws CreateException;
}
