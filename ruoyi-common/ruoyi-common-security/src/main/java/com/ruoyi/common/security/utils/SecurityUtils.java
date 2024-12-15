package com.ruoyi.common.security.utils;

import javax.servlet.http.HttpServletRequest;

import com.ruoyi.common.core.utils.JwtUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.constant.TokenConstants;
import com.ruoyi.common.core.context.MySecurityContextHolder;
import com.ruoyi.common.core.utils.ServletUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.system.api.model.LoginUser;

/**
 * 权限获取工具类
 * 
 * @author ruoyi
 */
public class SecurityUtils
{
    public static final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    /**
     * 获取用户ID
     */
    public static Long getUserId()
    {
        return getLoginUser().getUserid();
    }

    /**
     * 获取用户名称
     */
    public static String getUsername()
    {
        return getLoginUser().getUsername();
    }

    /**
     * 获取用户key
     */
    public static String getUserKey()
    {
        return JwtUtils.getUserKey(getLoginUser().getToken());
    }

    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser()
    {
        return (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取请求token
     */
    public static String getToken()
    {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request)
    {
        // 从header获取token标识
        String token = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        return replaceTokenPrefix(token);
    }

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token)
    {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    /**
     * 是否为管理员
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
