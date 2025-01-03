package com.ruoyi.forum.entities.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.forum.entities.Comment;
import lombok.Data;

import java.util.Date;
// 评论详情数据传输类，继承自评论类
@Data
public class CommentDetail extends Comment {
    @TableField(exist = false)
    private Date updatedTime;
    @TableField(exist = false)
    private Long replyToId;
    @TableField(exist = false)
    private String nickName;
    @TableField(exist = false)
    private String replyToName;
    @TableField(exist = false)
    private Boolean isMyComment=false;
    @TableField(exist = false)
    private String avatar;
}
