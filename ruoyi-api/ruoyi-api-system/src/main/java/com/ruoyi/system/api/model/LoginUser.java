package com.ruoyi.system.api.model;

import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.core.enums.UserStatus;
import com.ruoyi.system.api.config.CustomAuthorityDeserializer;
import com.ruoyi.system.api.domain.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户信息
 *
 * @author ruoyi
 */
public class LoginUser implements Serializable, UserDetails
{
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户名id
     */
    private Long userid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;


    /**
     * 用户信息
     */
    private SysUser sysUser;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Long getUserid()
    {
        return userid;
    }

    public void setUserid(Long userid)
    {
        this.userid = userid;
    }

    @JsonIgnore
    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(Objects.equals(sysUser.getUserType(), UserConstants.NORMAL_TYPE)&&Objects.equals(sysUser.getStatus(), UserConstants.NORMAL))
        {
            authorities.add(new SimpleGrantedAuthority(UserConstants.USER_ROLE));
        }
        else if(Objects.equals(sysUser.getUserType(), UserConstants.ADMIN_TYPE))
        {
            authorities.add(new SimpleGrantedAuthority(UserConstants.ADMIN_ROLE));
            authorities.add(new SimpleGrantedAuthority(UserConstants.USER_ROLE));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !UserStatus.DISABLE.getCode().equals(sysUser.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !UserStatus.DELETED.getCode().equals(sysUser.getStatus());
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Long getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Long loginTime)
    {
        this.loginTime = loginTime;
    }

    public Long getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Long expireTime)
    {
        this.expireTime = expireTime;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }


    public SysUser getSysUser()
    {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser)
    {
        this.sysUser = sysUser;
    }
}
