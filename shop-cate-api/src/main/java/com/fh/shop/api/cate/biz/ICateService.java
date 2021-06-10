package com.fh.shop.api.cate.biz;

import com.fh.shop.api.cate.param.CateParam;
import com.fh.shop.api.cate.po.Cate;
import com.fh.shop.common.ServerResponse;

public interface ICateService {

    public ServerResponse findAllList();

    ServerResponse addCate(Cate cate);

    ServerResponse deleteCate(String ids);

    ServerResponse findCate(Long id);

    ServerResponse updateCate(CateParam cateParam);

    ServerResponse addCateType(Cate cate);

    ServerResponse findCateChilds(Long id);
}
