package com.ruoyi.forum.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.forum.entities.BaseEntities.BaseEntity;
import lombok.Data;
// 投票（赞、踩）实体类，继承自基础实体类
@Data
@TableName("votes")
public class Vote extends BaseEntity {
    private Long postId;
    private Long userId;
    private Boolean upvote;
}
