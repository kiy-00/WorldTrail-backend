package com.ruoyi.forum.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.forum.entities.BaseEntities.BaseEntity;
import lombok.Data;

import java.util.Date;
// 收藏实体类，继承自基础实体类
@Data
@TableName("favorites")
public class Favorite extends BaseEntity {
    private Long postId;
    private Long userId;
    @TableField(exist = false)
    private Date updatedTime;
}
