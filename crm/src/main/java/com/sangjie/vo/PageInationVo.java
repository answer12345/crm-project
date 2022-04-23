package com.sangjie.vo;

import com.sangjie.workbench.domain.Activity;

import java.util.List;

public class PageInationVo<T> {
    private List<T> dataList;
    private int total;

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> activityList) {
        this.dataList = activityList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "PageInationVo{" +
                "dataList=" + dataList +
                ", total=" + total +
                '}';
    }
}
