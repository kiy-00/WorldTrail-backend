package com.ruoyi.forum.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.forum.entities.BaseEntities.BaseEntity;
import lombok.Data;

import java.util.Date;
// 帖子图片实体类，继承自基础实体类
@Data
@TableName("post_images")
public class PostImage extends BaseEntity {
    private Long postId;
    private String fileName;
    @TableField(exist = false)
    private Date updatedTime;
    @TableField(exist = false)
    private Date createdTime;
}
