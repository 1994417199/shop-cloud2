package com.fh.shop.api.cate.param;

import com.fh.shop.api.cate.po.Cate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CateParam implements Serializable {

    private Cate cate=new Cate();

    private String ids;

    private List<Long> idList=new ArrayList<>();

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public Cate getCate() {
        return cate;
    }

    public void setCate(Cate cate) {
        this.cate = cate;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
