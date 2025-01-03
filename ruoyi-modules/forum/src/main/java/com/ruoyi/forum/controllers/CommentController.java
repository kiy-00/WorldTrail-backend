package com.ruoyi.forum.controllers;


import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.security.annotation.InnerAuth;
import com.ruoyi.forum.entities.Comment;
import com.ruoyi.forum.entities.DTO.CommentDetail;
import com.ruoyi.forum.services.CommentService;
import com.ruoyi.system.api.model.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
*  评论业务controller
* */
@Controller
@ResponseBody
@RequestMapping("/forum/comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @PreAuthorize("hasRole('user')")
    @PostMapping("post")
    public R<Long> postComment(@RequestBody Comment comment) {
        return R.ok(commentService.postComment(comment));
    }

    @PostMapping("delete")
    public R<Integer> deleteComment(@RequestParam("commentId") Long commentId) {
        return R.ok(commentService.deleteComment(commentId));
    }

    @GetMapping("list")
    public R<List<CommentDetail>> listCommentByPost(@RequestParam("postId") Long postId) {
        return R.ok(commentService.listCommentByPost(postId));
    }
    @InnerAuth
    @GetMapping("content")
    public R<String> getCommentContent(@RequestParam("id") Long commentId) {
        Comment comment= (Comment) commentService.getComment(commentId);
        return R.ok(comment.getContent());
    }
    @InnerAuth
    @GetMapping()
    public R<Object> getComment(@RequestParam("id") Long commentId) {
        return R.ok(commentService.getComment(commentId));
    }
    @InnerAuth
    @PostMapping("update")
    public R<Integer> updateComment(@RequestBody StatusDTO statusDTO) {
        return R.ok(commentService.updateComment(statusDTO.getId(), statusDTO.getStatus()));
    }
}
