package com.ruoyi.forum.controllers;


import com.ruoyi.common.core.domain.R;
import com.ruoyi.forum.entities.Comment;
import com.ruoyi.forum.entities.DTO.CommentDetail;
import com.ruoyi.forum.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
