package com.bdqn.service.impl;

import com.bdqn.entity.AppVersion;
import com.bdqn.mapper.AppVersionMapper;
import com.bdqn.service.AppInfoService;
import com.bdqn.service.AppVersionService;
import com.bdqn.service.DataDictionaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AppVersionServiceImpl implements AppVersionService {
    @Resource
    private AppVersionMapper appVersionMapper;
    @Resource
    private AppInfoService appInfoService;

    @Resource
    private DataDictionaryService dataDictionaryService;

    /**
     * 01
     * 根据 id 查询版本
     *
     * @param id
     * @return
     */
    @Override
    public AppVersion queryById(Long id) {

        return appVersionMapper.selectByPrimaryKey(id);
    }

    /**
     * 填充属性
     *
     * @param appVersions
     */
    public void bindDefault(List<AppVersion> appVersions) {
        for (AppVersion appVersion : appVersions) {
            //填充 APP名称
            appVersion.setAppname(appInfoService.queryById(appVersion.getAppid()).getSoftwarename());

            //填充 发布状态
            appVersion.setPublishstatusname(dataDictionaryService.queryByCodeAndId("PUBLISH_STATUS", appVersion.getPublishstatus()).getValuename());
        }
    }

    /**
     * 02
     * 根据 appid 查询版本
     *
     * @return
     */
    @Override
    public List<AppVersion> queryByAppid(Long appid) {
        Example example = new Example(AppVersion.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("appid", appid);

        List<AppVersion> appVersions = appVersionMapper.selectByExample(example);
        //填充属性
        bindDefault(appVersions);
        return appVersions;
    }

    /**
     * 新增版本
     * @param appVersion
     * @return
     */
    @Override
    @Transactional
    public int insertVersion(AppVersion appVersion) {

        return appVersionMapper.insert(appVersion);
    }
}
