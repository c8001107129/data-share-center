package com.gw.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListPageUtil<E> {
    /**
     * 每页显示条数
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int pageCount;

    private int allCount;

    /**
     * 原集合
     */
    private List<E> data;

    public ListPageUtil(List<E> data, int pageSize) {
        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
            //throw new IllegalArgumentException("data must be not empty!");
        }
        this.data = data;
        this.pageSize = pageSize;
        this.allCount = data.size();
        this.pageCount = data.size()/pageSize;
        if(data.size()%pageSize!=0){
            this.pageCount++;
        }
    }
    /**
     * 得到分页后的数据
     *
     * @param pageNum 页码
     * @return 分页后结果
     */
    public List<E> getPagedList(int pageNum) {
        int fromIndex = (pageNum - 1) * pageSize;
        if (fromIndex >= data.size()) {
            return Collections.emptyList();
        }
        int toIndex = pageNum * pageSize;
        if (toIndex >= data.size()) {
            toIndex = data.size();
        }
        return data.subList(fromIndex, toIndex);
    }
    public int getPageSize() {
        return pageSize;
    }
    public List<E> getData() {
        return data;
    }
    public int getPageCount() {
        return pageCount;
    }

    public int getAllCount() {
        return allCount;
    }
}
