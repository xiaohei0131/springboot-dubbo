package com.sike.response;

import com.sike.bean.BaseBean;

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
}
