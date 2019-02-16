package com.sike.request;

import com.sike.bean.BaseBean;

public class PageReq extends BaseBean {
    /**
     * 每页记录数
     **/
    private int pageSize = 10;

    /**
     * 页码
     **/
    private int pageNum = 1;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
