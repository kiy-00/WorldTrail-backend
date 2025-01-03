package com.ruoyi.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.forum.entities.Comment;
import com.ruoyi.forum.entities.DTO.CommentDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;
// 评论 Mapper 接口
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("SELECT c.*, u.nick_name, u.avatar,reply_user.id AS reply_to_id,reply_user.nick_name AS reply_to_name " +
            "FROM comments c " +
            "JOIN sys_user u ON c.user_id = u.id " +
            "LEFT JOIN comments p ON c.parent_comment = p.id " +
            "LEFT JOIN sys_user reply_user ON p.user_id = reply_user.id " +
            "WHERE c.post_id = #{postId} " )
    List<CommentDetail> getCommentsWithUserInfo(@Param("postId") Long postId);
}
