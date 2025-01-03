package com.ruoyi.admin.controllers;

import com.ruoyi.admin.entities.Report;
import com.ruoyi.admin.services.ReportService;
import com.ruoyi.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/admin/report")
public class ReportController {
    @Autowired
    ReportService reportService;

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/list")
    public R<List<Report>> list() {
        return R.ok(reportService.listReports());
    }
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/review")
    public R<Integer> review(@RequestParam("id") Long id, @RequestParam("reply") String reply) {
        return R.ok(reportService.updateReport(id, reply));
    }
    @PreAuthorize("hasRole('user')")
    @PostMapping("/new")
    public R<Integer> report(@RequestBody Report report) {
        return R.ok(reportService.postReport(report));
    }

}
