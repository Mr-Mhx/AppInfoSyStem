package com.bdqn.service;

import com.bdqn.entity.DevUser;

public interface DevUserService {

    public DevUser login(String devcode,String devpassword);

}
