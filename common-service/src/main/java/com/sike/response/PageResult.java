package com.sike.response;

import com.github.pagehelper.PageInfo;
import com.sike.bean.BaseBean;

import java.util.List;

public class PageResult extends BaseBean {

    private long total = 0;
    private Object data;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static PageResult make(List list){
        PageInfo pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setData(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }
}
