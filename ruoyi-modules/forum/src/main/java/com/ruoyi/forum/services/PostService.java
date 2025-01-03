package com.ruoyi.forum.services;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.forum.entities.Comment;
import com.ruoyi.forum.entities.DTO.PostDetail;
import com.ruoyi.forum.entities.DTO.PostDigest;
import com.ruoyi.forum.entities.Post;
import com.ruoyi.forum.entities.PostImage;
import com.ruoyi.forum.entities.Vote;
import com.ruoyi.forum.mapper.CommentMapper;
import com.ruoyi.forum.mapper.PostImagesMapper;
import com.ruoyi.forum.mapper.PostMapper;
import com.ruoyi.forum.mapper.VoteMapper;
import com.ruoyi.system.api.RemoteFileService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysFile;
import com.ruoyi.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
// 帖子服务实现类
@Service
public class PostService {

    @Autowired
    PostMapper postMapper;
    @Autowired
    VoteMapper voteMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    PostImagesMapper postImagesMapper;
    @Autowired
    private RemoteFileService remoteFileService;
    @Autowired
    private RemoteUserService remoteUserService;

    public static final int PAGE_SIZE = 10;



    public Long postPost(Post post, List<MultipartFile> files) {
        Long uid = SecurityUtils.getUserId();
        List<PostImage> postImageList=new ArrayList<>();
        post.setUserId(uid);
        post.setDeleted(false);
        postMapper.insert(post);
        if(files!=null&&!files.isEmpty()) {
            for (MultipartFile file : files) {
                if(file.getSize()>0) {
                    SysFile fileInfo= remoteFileService.upload(file,SecurityConstants.INNER).getData();
                    PostImage postImage = new PostImage();
                    postImage.setPostId(post.getId());
                    postImage.setFileName(fileInfo.getUrl());
                    postImageList.add(postImage);
                }
            }
            postImagesMapper.insert(postImageList);
        }

        return post.getId();
    }


    public PostDetail getPost(Long id) {
        Post post=postMapper.selectById(id);
        if(post!=null && !post.getDeleted()){
            PostDetail postDetail=new PostDetail(post);
            LoginUser loginUser=remoteUserService.getUserInfo(post.getUserId(), SecurityConstants.INNER).getData();
            postDetail.setUsername(loginUser.getSysUser().getNickName());
            postDetail.setVoteCount(Math.toIntExact(voteMapper
                    .selectCount(new QueryWrapper<Vote>()
                            .eq("user_id", loginUser.getUserid()))
            ));
            postDetail.setUserAvatarUrl(loginUser.getSysUser().getAvatar());
            postDetail.setUrls(postImagesMapper.selectList(new QueryWrapper<PostImage>()
                    .eq("post_id", postDetail.getId())
            ).stream().map(PostImage::getFileName).collect(Collectors.toList()));
            return postDetail;
        }
        return null;
    }

    public Long countPost() {
        return postMapper.selectCount(new QueryWrapper<Post>().eq("deleted", false));
    }
    public Long searchPostCount(String keyword) {
        return postMapper.selectCount(new QueryWrapper<Post>().like("title", keyword).eq("deleted", false));
    }

    public Integer deletePost(Long id) {
        Post post=postMapper.selectById(id);
        Long uid = SecurityUtils.getUserId();
        if(Objects.equals(post.getUserId(), uid)) {
            post.setDeleted(true);
            postMapper.updateById(post);
            return 200;
        }
        return 500;
    }


    public List<PostDigest> listPost(Integer page) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", false).orderByDesc("id");
        return pageSearch(PAGE_SIZE,page,queryWrapper);
    }


    public List<PostDigest> listPostByUser(Long uid,Integer page) {
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",uid).eq("deleted", false).orderByDesc("id");
        return pageSearch(PAGE_SIZE,page,wrapper);
    }


    public List<PostDigest> randomPost() {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", false).orderByAsc("RAND()").last("LIMIT 10");
        return pageSearch(10, 1, queryWrapper);
    }


    public List<PostDigest> searchPost(String keyword,Integer page) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", keyword).eq("deleted", false).orderByDesc("id");
        return pageSearch(PAGE_SIZE,page,queryWrapper);
    }

    private List<PostDigest> pageSearch(Integer pageSize,Integer page,QueryWrapper<Post> queryWrapper){
        Page<Post> postPage = new Page<>(page, pageSize);
        Page<Post> resultPage = postMapper.selectPage(postPage, queryWrapper);
        List<Post> posts = resultPage.getRecords();
        return posts.stream()
                .map(post -> {
                    PostDigest postDigest = new PostDigest();
                    postDigest.setId(post.getId());
                    postDigest.setTitle(post.getTitle());
                    postDigest.setUserAvatarUrl(remoteUserService.getUserInfo(post.getUserId(), SecurityConstants.INNER).getData().getSysUser().getAvatar());
                    postDigest.setCommentCount(Math.toIntExact(commentMapper
                            .selectCount(new QueryWrapper<Comment>()
                                    .eq("post_id", post.getId()))
                    ));
                    postDigest.setVoteCount(Math.toIntExact(voteMapper
                            .selectCount(new QueryWrapper<Vote>()
                                    .eq("post_id", post.getId()))
                    ));
                    postDigest.setCreatedTime(post.getCreatedTime());
                    return postDigest;
                })
                .collect(Collectors.toList());
    }
}
