package com.bdqn.service;

import com.bdqn.entity.AppVersion;

import java.util.List;

public interface AppVersionService {

    // 根据 id查询
    public AppVersion queryById(Long id);

    //根据 appid 查询版本
    public List<AppVersion> queryByAppid(Long appid);

    //新增版本
    public int insertVersion(AppVersion appVersion);
}
