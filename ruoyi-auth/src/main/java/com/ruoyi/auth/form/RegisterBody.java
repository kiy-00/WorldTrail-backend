package com.ruoyi.auth.form;

import lombok.Data;

/**
 * 用户注册对象
 * 
 * @author ruoyi
 */
@Data
public class RegisterBody extends LoginBody
{
    private String emailCode;
    private String email;
}
