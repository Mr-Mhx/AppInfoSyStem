package com.bdqn.service.impl;

import com.bdqn.entity.AppCategory;
import com.bdqn.mapper.AppCategoryMapper;
import com.bdqn.service.AppCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class AppCategoryServiceImpl implements AppCategoryService {
    @Resource
    private AppCategoryMapper appCategoryMapper;

    @Override
    public AppCategory queryByParentId(Long parentid) {
        return appCategoryMapper.selectByPrimaryKey(parentid);
    }


    /**
     * 02
     * 查询分类级别
     * @param parentid
     * @return
     */
    @Override
    public List<AppCategory> queryAppCategory(Long parentid) {
        Example example = new Example(AppCategory.class);
        Example.Criteria criteria = example.createCriteria();

        if (parentid == null) {
            //一级
            criteria.andIsNull("parentid");
        } else {
            //其它
            criteria.andEqualTo("parentid", parentid);
        }

        return appCategoryMapper.selectByExample(example);
    }
}
