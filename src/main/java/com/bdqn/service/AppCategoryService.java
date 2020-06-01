package com.bdqn.service;

import com.bdqn.entity.AppCategory;

import java.util.List;

public interface AppCategoryService {

    public AppCategory queryByParentId(Long parentid);


    public List<AppCategory> queryAppCategory(Long parentid);
}
