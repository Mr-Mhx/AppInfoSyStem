package com.bdqn.service;

import com.bdqn.entity.AppInfo;
import com.github.pagehelper.PageInfo;

public interface AppInfoService {


    //APP列表
    public PageInfo<AppInfo> queryAppInfo(Long devid, Integer pageIndex, AppInfo appInfo);

    //查询APK
    public AppInfo queryApkexist(String apkname);

    //添加
    public int insertApp(AppInfo appInfo);

    //根据 id查询
    public AppInfo queryById(Long id);

    //修改
    public int updateApp(AppInfo appInfo);

}
