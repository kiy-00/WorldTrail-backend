package com.ruoyi.forum.controllers;


import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.security.annotation.InnerAuth;
import com.ruoyi.forum.entities.DTO.PostDetail;
import com.ruoyi.forum.entities.DTO.PostDigest;
import com.ruoyi.forum.entities.Favorite;
import com.ruoyi.forum.entities.Post;
import com.ruoyi.forum.entities.Vote;
import com.ruoyi.forum.services.FavoriteService;
import com.ruoyi.forum.services.PostService;
import com.ruoyi.forum.services.VoteService;
import com.ruoyi.system.api.model.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  发帖业务controller
 * */
@Controller
@ResponseBody
@RequestMapping("/forum/post")
public class PostController {
    // 发帖业务
    @Autowired
    PostService postService;
    // 点赞点踩业务
    @Autowired
    VoteService voteService;
    // 收藏业务
    @Autowired
    FavoriteService favoriteService;
    // 发帖
    @PreAuthorize("hasRole('user')")
    @PostMapping("new")
    public R<Long> postPost(@ModelAttribute Post post,
                               @RequestParam("files") List<MultipartFile> files) {
        return R.ok(postService.postPost(post, files));
    }
    // 查看某帖子详情
    @GetMapping("get")
    public R<PostDetail> getPost(Long id) {
        PostDetail postDetail = postService.getPost(id,false);
        return R.ok(postDetail);
    }
    @InnerAuth
    @GetMapping("adminGet")
    public R<PostDetail> adminGetPost(Long id) {
        PostDetail postDetail = postService.getPost(id,true);
        return R.ok(postDetail);
    }
    @InnerAuth
    @GetMapping("content")
    public R<String> getPostContent(Long id) {
        PostDetail postDetail = postService.getPost(id,false);
        return R.ok(postDetail.getTitle()+postDetail.getContent());
    }

    // 删除帖子
    @PostMapping("delete")
    public R<Integer> deletePost(@RequestParam("id") Long id) {
        return R.ok(postService.deletePost(id));
    }
    @InnerAuth
    @PostMapping("update")
    public R<Integer> updatePost(@RequestBody StatusDTO statusDTO) {
        return R.ok(postService.updatePost(statusDTO.getId(), statusDTO.getStatus()));
    }

    @GetMapping("count")
    public R<Long> countPost() {
        return R.ok(postService.countPost());
    }
    // 罗列所有帖子中，第n页的帖子
    @GetMapping("list")
    public R<List<PostDigest>> listPost(@RequestParam("page") Integer page) {
        return R.ok(postService.listPost(page));
    }
    // 随机一页帖子
    @GetMapping("random")
    public R<List<PostDigest>> randomPost() {
        return R.ok(postService.randomPost());
    }
    // 罗列某用户所有帖子中第n页的帖子
    @GetMapping("user")
    public R<List<PostDigest>> listPostByUser(@RequestParam("uid") Long uid,@RequestParam("page") Integer page) {
        return R.ok(postService.listPostByUser(uid,page));
    }
    @GetMapping("search-count")
    public R<Long> searchPostCount(@RequestParam("keyword")String keyword) {
        return R.ok(postService.searchPostCount(keyword));
    }
    // 关键词搜索帖子
    @GetMapping("search")
    public R<List<PostDigest>> searchPost(@RequestParam("keyword")String keyword,@RequestParam("page") Integer page) {
        return R.ok(postService.searchPost(keyword,page));
    }
    // 点赞点踩
    @PreAuthorize("hasRole('user')")
    @PostMapping("vote")
    public R<Integer> votePost(@RequestBody Vote vote) {
        return R.ok(voteService.vote(vote));
    }
    // 是否赞或踩过该帖子
    @GetMapping("isVoted")
    public R<Integer> isVoted(@RequestParam("postId") Long postId) {
        return R.ok(voteService.isPostVoted(postId));
    }
    // 获取帖子获赞总数
    @GetMapping("voteCount")
    public R<Integer> voteCount(@RequestParam("postId") Long postId) {
        return R.ok(voteService.getVoteCount(postId));
    }
    // 收藏帖子
    @PostMapping("favorite")
    public R<Integer> favoritePost(@RequestParam("postId") Long postId) {
        return R.ok(favoriteService.postFavorite(postId));
    }
    // 删除收藏项
    @PostMapping("deleteFavorite")
    public R<Integer> deleteFavoritePost(@RequestParam("postId") Long id) {
        return R.ok(favoriteService.deleteFavorite(id));
    }
    // 罗列自己的收藏
    @GetMapping("listFavorite")
    public R<List<Favorite>> listFavoritePost() {
        return R.ok(favoriteService.listFavoriteByUser());
    }
}
