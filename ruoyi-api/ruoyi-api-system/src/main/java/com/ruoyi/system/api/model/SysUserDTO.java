package com.ruoyi.system.api.model;

import com.ruoyi.system.api.domain.SysUser;
import lombok.Data;

@Data
public class SysUserDTO extends SysUser {
    private String emailCode;
}
