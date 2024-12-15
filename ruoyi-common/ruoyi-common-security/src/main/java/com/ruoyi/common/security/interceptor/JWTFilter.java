package com.ruoyi.common.security.interceptor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.constant.TokenConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.auth.AuthUtil;
import com.ruoyi.system.api.model.LoginUser;

import java.io.IOException;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author ruoyi
 */
//public class HeaderInterceptor implements AsyncHandlerInterceptor
@Component
public class JWTFilter extends OncePerRequestFilter
{
    public LoginUser getLoginUserByToken(HttpServletRequest request) {
        //SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID));
        //SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USERNAME));
        //SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.USER_KEY));
        // 从请求中获取token
        //String token = SecurityUtils.getToken();
        String token = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        if (StringUtils.isNotEmpty(token))
        {
            if( token.startsWith(TokenConstants.PREFIX))
                token = token.replaceFirst(TokenConstants.PREFIX, "");
            // 根据token到redis去查询user对象
            LoginUser loginUser = AuthUtil.getLoginUser(token);
            // user对象不为空（查到了）
            if (StringUtils.isNotNull(loginUser))
            {
                // 验证当前用户有效期
                AuthUtil.verifyLoginUserExpire(loginUser);
                // 安全上下文设置用户信息
                //SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
                return loginUser;
            }
        }
        return null;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LoginUser loginUser = getLoginUserByToken(request);
        if(loginUser != null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        filterChain.doFilter(request, response);

    }
}
