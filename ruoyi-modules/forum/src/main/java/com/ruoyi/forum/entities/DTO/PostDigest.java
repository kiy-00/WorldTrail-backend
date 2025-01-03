package com.ruoyi.forum.entities.DTO;


import com.ruoyi.forum.entities.BaseEntities.BaseEntity;
import lombok.Data;
// 帖子摘要数据传输类，继承自基础实体类
@Data
public class PostDigest extends BaseEntity {
    private String title;
    private String username;
    private String userAvatarUrl;
    private Integer commentCount;
    private Integer voteCount;
}
