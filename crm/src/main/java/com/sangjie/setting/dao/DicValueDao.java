package com.sangjie.setting.dao;

import com.sangjie.setting.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValueByCode(String code);
}
