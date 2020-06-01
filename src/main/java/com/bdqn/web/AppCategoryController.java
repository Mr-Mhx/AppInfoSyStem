package com.bdqn.web;

import com.bdqn.entity.AppCategory;
import com.bdqn.service.AppCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/categoryCon")
public class AppCategoryController {
    @Resource
    private AppCategoryService appCategoryService;


    /**
     * 01
     * 联动  二、三级分类数据
     *
     * @return
     */
    @RequestMapping("/queryCategoryLevel") @ResponseBody
    public List<AppCategory> queryCategoryLevel(Long pid) {

        return appCategoryService.queryAppCategory(pid);
    }

}
