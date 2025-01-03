package com.ruoyi.forum.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.forum.entities.BaseEntities.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
// 帖子实体类，继承自基础实体类
@Data
@TableName("Posts")
public class Post extends BaseEntity implements Serializable {

    @TableField("user_id")
    private Long userId;

    private String title;

    private String content;

    private Boolean deleted;
    @TableField(exist = false)
    private Date updatedTime;
}
