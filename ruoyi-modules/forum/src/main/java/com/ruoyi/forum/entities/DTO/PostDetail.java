package com.ruoyi.forum.entities.DTO;


import com.ruoyi.forum.entities.Post;
import lombok.Data;

import java.util.List;
// 帖子详情数据传输类，继承自帖子类
@Data
public class PostDetail extends Post {
    private String username;
    private String userAvatarUrl;
    private Integer voteCount;
    private List<String> urls;
    public PostDetail(Post post) {
        this.setId(post.getId());
        this.setUserId(post.getUserId());
        this.setTitle(post.getTitle());
        this.setContent(post.getContent());
        this.setDeleted(post.getDeleted());
        this.setCreatedTime(post.getCreatedTime());
        this.setUpdatedTime(post.getUpdatedTime());
    }
}
