package com.ruoyi.word.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.word.service.CheckinService;

import java.util.List;

@RestController
@RequestMapping("/word/user")
public class CheckinController {

    @Autowired
    private CheckinService checkinService;


    @PostMapping("/checkin")
    @PreAuthorize("hasRole('user')")
    public AjaxResult dailyCheckin() {
        AjaxResult ajaxResult = AjaxResult.success("签到成功");
        ajaxResult.put("checkinDays", checkinService.dailyCheckin());
        return ajaxResult;
    }

    @GetMapping("/checkin-days")
    @PreAuthorize("hasRole('user')")
    public AjaxResult getCheckinStreak() {
        AjaxResult ajaxResult = AjaxResult.success("获取签到天数成功");
        ajaxResult.put("checkinDays", checkinService.getCheckinStreak());
        return ajaxResult;
    }

    @GetMapping("/checkin-dates")
    public R<List<Integer>> getCheckinDates(@RequestParam("year") Integer year, @RequestParam("month") Integer month) {
        return R.ok(checkinService.getCheckinDates(year, month));
    }
}
