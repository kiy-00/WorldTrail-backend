package com.ruoyi.common.security.service;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private SysPasswordService passwordService;
    @Autowired
    private RemoteUserService remoteUserService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, ServiceException {
        R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException("请求失败");
        }
        return userResult.getData();
    }
}
