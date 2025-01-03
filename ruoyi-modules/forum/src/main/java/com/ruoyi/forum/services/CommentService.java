package com.ruoyi.forum.services;

import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.forum.entities.Comment;
import com.ruoyi.forum.entities.DTO.CommentDetail;
import com.ruoyi.forum.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
// 评论服务实现类
@Service
public class CommentService  {
    @Autowired
    CommentMapper commentMapper;


    public Long postComment(Comment comment) {
        Long uid= SecurityUtils.getUserId();
        comment.setUserId(uid);
        comment.setDeleted(false);
        commentMapper.insert(comment);
        return comment.getId();
    }


    public Integer deleteComment(Long id) {
        Comment comment = commentMapper.selectById(id);
        Long uid= SecurityUtils.getUserId();
        if(comment.getUserId().equals(uid)){
            comment.setDeleted(true);
            commentMapper.updateById(comment);
            return 200;
        }
        return 0;
    }


    public List<CommentDetail> listCommentByPost(Long postId) {
        List<CommentDetail> list= commentMapper.getCommentsWithUserInfo(postId);
        Long uid= SecurityUtils.getUserId();
        for(CommentDetail c : list){
            if(c.getUserId().equals(uid))
                c.setIsMyComment(true);
            if(c.getDeleted())
                c.setContent("[该评论已被删除]");
        }
        return list;
    }


}
