package com.fh.shop.api.cate.controller;

import com.fh.shop.api.cate.biz.ICateService;
import com.fh.shop.api.cate.param.CateParam;
import com.fh.shop.api.cate.po.Cate;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/api")

@Slf4j
public class CateController {

    @Value("${server.port}")
    private String port;

    @Resource(name = "cateService")
    private ICateService cateService;

    @RequestMapping(value = "/cate/findList",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findAllList(){
        log.info("端口号",port);
        System.out.println("git 上传测试");
        return cateService.findAllList();
    }

    @ApiOperation("增加分类")
    @RequestMapping(value = "/cate/addCate",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCate(Cate cate){
        return cateService.addCate(cate);
    }

    @ApiOperation("增加分类类型")
    @RequestMapping(value = "/cate/addCateType",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCateType(Cate cate){
        return cateService.addCateType(cate);
    }

    @ApiOperation("删除分类")
    @RequestMapping(value = "/cate/deleteCate",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse deleteCate(String ids){
        return cateService.deleteCate(ids);
    }

    @ApiOperation("根据id查询分类")
    @RequestMapping(value = "/cate/findCate",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse findCate(Long id){
        return cateService.findCate(id);
    }

    @ApiOperation("修改分类")
    @RequestMapping(value = "/cate/updateCate",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateCate(CateParam cateParam){
        return cateService.updateCate(cateParam);
    }

    @ApiOperation("查询分类的子级")
    @RequestMapping(value = "/cate/findCateChilds",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findCateChilds(Long id){
        return cateService.findCateChilds(id);
    }

}
