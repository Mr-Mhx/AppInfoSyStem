package com.bdqn.service.impl;

import com.bdqn.entity.AppInfo;
import com.bdqn.entity.AppVersion;
import com.bdqn.mapper.AppInfoMapper;
import com.bdqn.service.AppCategoryService;
import com.bdqn.service.AppInfoService;
import com.bdqn.service.AppVersionService;
import com.bdqn.service.DataDictionaryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AppInfoServiceImpl implements AppInfoService {

    @Resource
    private AppInfoMapper appInfoMapper;

    @Resource
    private DataDictionaryService dataDictionaryService;

    @Resource
    private AppCategoryService appCategoryService;

    @Resource
    private AppVersionService appVersionService;

    /**
     * 01 根据 devid查询
     * 每一个开发者登录后只查看自己负责板块
     *
     * @param devid
     * @return
     */
    @Override
    public PageInfo<AppInfo> queryAppInfo(Long devid, Integer pageIndex, AppInfo info) {
        //1.设置分页
        PageHelper.startPage(pageIndex, 5);


        //2.查询数据
        Example example = new Example(AppInfo.class);
        Example.Criteria criteria = example.createCriteria();


        if (info.getSoftwarename() != null) {
            criteria.andLike("softwarename", "%" + info.getSoftwarename() + "%");
        }
        if (info.getStatus() != 0 && info.getStatus() != null) {
            criteria.andEqualTo("status", info.getStatus());
        }
        if (info.getFlatformid() != 0 && info.getFlatformid() != null) {
            criteria.andEqualTo("flatformid", info.getFlatformid());
        }
        if (info.getCategorylevel1() != 0 && info.getCategorylevel1() != null) {
            criteria.andEqualTo("categorylevel1", info.getCategorylevel1());
        }
        if (info.getCategorylevel2() != 0 && info.getCategorylevel2() != null) {
            criteria.andEqualTo("categorylevel2", info.getCategorylevel2());
        }
        if (info.getCategorylevel3() != 0 && info.getCategorylevel3() != null) {
            criteria.andEqualTo("categorylevel3", info.getCategorylevel3());
        }

        // 查看开发人员所管理的APP
        criteria.andEqualTo("devid", devid);
        List<AppInfo> appInfoList = appInfoMapper.selectByExample(example);

        //3.数据处理
        PageInfo<AppInfo> pageInfo = new PageInfo<>(appInfoList);

        //填充数据
        binData(appInfoList);

        return pageInfo;
    }

    /**
     * 02
     * 列表数据填充
     *
     * @param appInfos
     */
    public void binData(List<AppInfo> appInfos) {
        appInfos.forEach(appInfo -> {
            //1. 填充所属平台
            appInfo.setFlatformname(dataDictionaryService.queryByCodeAndId("APP_FLATFORM", appInfo.getFlatformid()).getValuename());
            //2. 填充分类  一级 二级 三级
            appInfo.setCategorylevel1name(appCategoryService.queryByParentId(appInfo.getCategorylevel1()).getCategoryname());
            appInfo.setCategorylevel2name(appCategoryService.queryByParentId(appInfo.getCategorylevel2()).getCategoryname());
            appInfo.setCategorylevel3name(appCategoryService.queryByParentId(appInfo.getCategorylevel3()).getCategoryname());
            //3.填充状态
            appInfo.setStatusname(dataDictionaryService.queryByCodeAndId("APP_STATUS", appInfo.getStatus()).getValuename());
            AppVersion appVersion = appVersionService.queryById(appInfo.getVersionid());
            if (appVersion != null) {
                appInfo.setVersionno(appVersion.getVersionno());
            }
        });
    }

    /**
     * 验证APK
     * @param apkname
     * @return
     */
    public AppInfo queryApkexist(String apkname) {

        AppInfo appInfo = new AppInfo();
        appInfo.setApkname(apkname);

        return appInfoMapper.selectOne(appInfo);
    }

    @Override
    public AppInfo queryById(Long id) {
        AppInfo appInfo = appInfoMapper.selectByPrimaryKey(id);
        //填充属性
        binData(Arrays.asList(appInfo));
        return appInfo;
    }

    /**
     * 新增
     * @param appInfo
     * @return
     */
    @Override
    @Transactional
    public int insertApp(AppInfo appInfo) {
        return appInfoMapper.insert(appInfo);
    }

    /**
     * 修改
     * @param appInfo
     * @return
     */
    @Override
    @Transactional
    public int updateApp(AppInfo appInfo) {
        return appInfoMapper.updateByPrimaryKeySelective(appInfo);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int deleteApp(Long id) {
        return appInfoMapper.deleteByPrimaryKey(id);
    }
}
