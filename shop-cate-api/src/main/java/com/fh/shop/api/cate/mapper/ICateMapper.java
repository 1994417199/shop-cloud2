package com.fh.shop.api.cate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.cate.param.CateParam;
import com.fh.shop.api.cate.po.Cate;

import java.util.List;

public interface ICateMapper extends BaseMapper<Cate> {
    List<Cate> findAllList();

    void addCate(Cate cate);

    void deleteCate(List<Long> idsList);

    Cate findCate(Long id);

    void updateCate(Cate cate);

    void updateTypeInfo(CateParam cateParam);

    void addCateType(Cate cate);
}
