package com.ruoyi.system.controller;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.redis.service.RedisService;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.model.SysUserDTO;
import com.ruoyi.system.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.annotation.InnerAuth;

import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.LoginUser;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 用户信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;


    @Autowired
    private ISysConfigService configService;


    @Autowired
    private EmailService emailService;

    @Autowired
    RedisService redisService;


    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/info/{username}")
    public R<LoginUser> info(@PathVariable("username") String username)
    {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (StringUtils.isNull(sysUser))
        {
            return R.fail("用户名或密码错误");
        }
        LoginUser sysUserVo = new LoginUser(sysUser);
        return R.ok(sysUserVo);
    }
    @InnerAuth
    @GetMapping("/info/id/{uid}")
    public R<LoginUser> info(@PathVariable("uid") Long uid)
    {
        SysUser sysUser = userService.selectUserById(uid);
        if (StringUtils.isNull(sysUser))
        {
            return R.fail("不存在用户");
        }
        LoginUser sysUserVo = new LoginUser(sysUser);
        return R.ok(sysUserVo);
    }

    /**
     * 注册用户信息
     */
    @InnerAuth
    @PostMapping("/register")
    public R<Integer> register(@RequestBody SysUserDTO sysUser)
    {
        String username = sysUser.getUserName();
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return R.fail("当前系统没有开启注册功能！");
        }
        String trueCode=redisService.getCacheObject(sysUser.getEmail()+"-"+ UserConstants.BUSINESS_TYPE_REGISTER);
        if(sysUser.getEmailCode()==null||!Objects.equals(sysUser.getEmailCode(),redisService.getCacheObject(sysUser.getEmail()+"-"+ UserConstants.BUSINESS_TYPE_REGISTER))){
            System.out.println(trueCode);
            return R.fail("验证码错误");
        }
        int result= userService.registerUser(sysUser);
        redisService.deleteObject(sysUser.getEmail()+"-"+ UserConstants.BUSINESS_TYPE_REGISTER);
        if (result == 0)
        {
            return R.fail("注册用户'" + username + "'失败，登录账号已存在");
        }
        return R.ok(result);
    }



    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    @PreAuthorize("hasRole('user')")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getSysUser();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        return ajax;
    }

    /**
     * 邮箱验证码
     */
    @PostMapping("/emailCode")
    public AjaxResult emailCode(String email,String businessType)
    {
        if (StringUtils.isEmpty(email))
        {
            return AjaxResult.error("邮箱不能为空");
        }
        int code= new Random().nextInt(9000)+1000;
        redisService.setCacheObject(email+"-"+businessType, Integer.toString(code), 600L, TimeUnit.SECONDS);
        try {
            emailService.sendEmail(email,"身份验证", "您的验证码是 " + code+"， 请在10分钟内输入。");
        }
        catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("发送邮件失败，请稍后再试");
        }
        return AjaxResult.success();
    }



}
