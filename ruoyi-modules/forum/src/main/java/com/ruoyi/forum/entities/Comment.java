package com.ruoyi.forum.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.forum.entities.BaseEntities.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
// 评论实体类，继承自基础实体类
@Data
@TableName("Comments")
public class Comment extends BaseEntity implements Serializable {

    private Long userId;

    private Long postId;

    private Long parentComment;

    private String content;

    private Character status='0';

    @TableField(exist = false)
    private Date updatedTime;


}
