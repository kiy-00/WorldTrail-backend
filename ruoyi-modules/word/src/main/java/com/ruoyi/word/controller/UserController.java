package com.ruoyi.word.controller;

import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.word.service.UserService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/word/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/checkin")
    public AjaxResult dailyCheckin() {
        AjaxResult ajaxResult = AjaxResult.success("签到成功");
        ajaxResult.put("checkinDays", userService.dailyCheckin());
        return ajaxResult;
    }

    @GetMapping("/checkin-days")
    public AjaxResult getCheckinStreak() {
        AjaxResult ajaxResult = AjaxResult.success("获取签到天数成功");
        ajaxResult.put("checkinDays", userService.getCheckinStreak());
        return ajaxResult;
    }
}
