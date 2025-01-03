package com.ruoyi.admin.controllers;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.RemoteForumService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.model.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/admin/manage")
public class ManageController {
    @Autowired
    private RemoteForumService remoteForumService;
    @Autowired
    private RemoteUserService remoteUserService;

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/post")
    public R<Object> post(Long id) {
        R<Object> result = remoteForumService.getPost(id, SecurityConstants.INNER);
        return result;
    }
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/comment")
    public R<Object> comment(Long id) {
        R<Object> result = remoteForumService.getComment(id, SecurityConstants.INNER);
        return result;
    }
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/comment")
    public R<Integer> updateComment(@RequestBody StatusDTO status) {
        R<Integer> result = remoteForumService.updateComment(status, SecurityConstants.INNER);
        return result;
    }
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/post")
    public R<Integer> updatePost(@RequestBody StatusDTO status) {
        R<Integer> result = remoteForumService.updatePost(status, SecurityConstants.INNER);
        return result;
    }
}
