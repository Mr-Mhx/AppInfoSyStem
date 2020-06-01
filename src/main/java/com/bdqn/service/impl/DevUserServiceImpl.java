package com.bdqn.service.impl;

import com.bdqn.entity.DevUser;
import com.bdqn.mapper.DevUserMapper;
import com.bdqn.service.DevUserService;
import com.bdqn.utils.AppUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)  //默认查询操作
public class DevUserServiceImpl implements DevUserService {



    @Resource
    private DevUserMapper devUserMapper;


    /**
     * 01
     * 登录
     * @param devcode
     * @param devpassword
     * @return
     */
    @Override
    public DevUser login(String devcode,String devpassword) {
        DevUser devUser = new DevUser();
        devUser.setDevcode(devcode);
        try {
            //密码加密
            devUser.setDevpassword(AppUtils.EncoderByMd5(devpassword));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devUserMapper.selectOne(devUser);
    }


}
