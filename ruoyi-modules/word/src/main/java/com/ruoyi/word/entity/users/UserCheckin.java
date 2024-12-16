package com.ruoyi.word.entity.users;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.system.api.domain.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@TableName("user_checkin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCheckin {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long user_id;

    private Short checkinDays;

    private Date updateTime;
}