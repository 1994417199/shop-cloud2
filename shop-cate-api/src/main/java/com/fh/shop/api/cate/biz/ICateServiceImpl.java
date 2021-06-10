package com.fh.shop.api.cate.biz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.cate.mapper.ICateMapper;
import com.fh.shop.api.cate.param.CateParam;
import com.fh.shop.api.cate.po.Cate;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("cateService")
public class ICateServiceImpl implements ICateService {

    @Autowired
    private ICateMapper cateMapper;
//    @Autowired
//    private ITypeMapper typeMapper;


    @Override
    public ServerResponse findAllList() {
        String cateList1 = RedisUtil.get("cateList");
        if(StringUtils.isNotEmpty(cateList1)){
            //将json转换为java对象
            List<Cate> cates = JSON.parseArray(cateList1, Cate.class);
            return ServerResponse.success(cates);
        }
        List<Cate> cateList = cateMapper.selectList(null);
        //将java对象转换为json对象
        String s = JSON.toJSONString(cateList);
        //放到缓存中
        RedisUtil.set("cateList",s);
        return ServerResponse.success(cateList);
    }

    @Override
    public ServerResponse addCate(Cate cate) {
        cateMapper.addCate(cate);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteCate(String ids) {
        if (StringUtils.isEmpty(ids)){
            return ServerResponse.error(ResponseEnum.CATE_IS_NULL);
        }
        String[] idsArr = ids.split(",");
        List<Long> idsList=new ArrayList<>();
        for (String cate : idsArr) {
            idsList.add(Long.parseLong(cate));
        }
        cateMapper.deleteCate(idsList);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findCate(Long id) {
//        Cate cate=cateMapper.findCate(id);
//        //获取所有的类型信息
//        List<Type> allType= typeMapper.findAllType();
//        CateVo cateVo = new CateVo();
//        cateVo.setCate(cate);
//        cateVo.setTypeList(allType);
//        return ServerResponse.success(cateVo);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateCate(CateParam cateParam) {
        Cate cate = cateParam.getCate();
        if (cateParam.getIds().isEmpty()){
            //更新分类信息
            cateMapper.updateCate(cate);
            return ServerResponse.success();
        }
        cateMapper.updateCate(cate);
        //将当前分类下的所有子子孙孙类型的typeId以及typeName都覆盖
        String ids=cateParam.getIds();
        String[] idArr = ids.split(",");
        List<Long> idList=new ArrayList<>();
        for (String id : idArr) {
            idList.add(Long.parseLong(id));
        }
        cateParam.setIdList(idList);
        cateMapper.updateTypeInfo(cateParam);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse addCateType(Cate cate) {
        cateMapper.addCateType(cate);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findCateChilds(Long id) {
        //将当前id作为父id
        QueryWrapper<Cate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fatherId",id);
        List<Cate> cateList = cateMapper.selectList(queryWrapper);
        return ServerResponse.success(cateList);
    }
}
