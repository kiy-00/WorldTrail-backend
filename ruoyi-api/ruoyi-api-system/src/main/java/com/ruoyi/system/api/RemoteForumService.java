package com.ruoyi.system.api;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.factory.RemoteForumFallbackFactory;
import com.ruoyi.system.api.factory.RemoteLogFallbackFactory;
import com.ruoyi.system.api.model.StatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteForumService", value = ServiceNameConstants.FORUM_SERVICE, fallbackFactory = RemoteForumFallbackFactory.class)
public interface RemoteForumService {
    @GetMapping("/forum/post/adminGet")
    public R<Object> getPost(@RequestParam("id") Long id, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    @GetMapping("/forum/comment")
    public R<Object> getComment(@RequestParam("id") Long commentId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    @PostMapping("/forum/comment/update")
    public R<Integer> updateComment(@RequestBody StatusDTO statusDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    @PostMapping("/forum/post/update")
    public R<Integer> updatePost(@RequestBody StatusDTO statusDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    @GetMapping("/forum/post/content")
    public R<String> getPostContent(@RequestParam("id") Long id, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    @GetMapping("/forum/comment/content")
    public R<String> getCommentContent(@RequestParam("id") Long id, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
