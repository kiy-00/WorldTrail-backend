package com.ruoyi.word.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.RemoteFileService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.word.domain.Word;
import com.ruoyi.word.service.WordInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/word")
public class WordInfoController extends BaseController {
    @Autowired
    private WordInfoService wordInfoService;

    private RemoteUserService remoteUserService;

    private RemoteFileService remoteFileService;



    @GetMapping("/list")
    public AjaxResult list(@RequestParam("creator") String creator)
    {
        Long uid=SecurityUtils.getUserId();
        return AjaxResult.success("查询成功",wordInfoService.selectWordByCreatorId(uid));
    }
    @RequiresPermissions("system:notice:list")
    @PostMapping("/add")
    public AjaxResult addWord(@ModelAttribute Word word, @RequestParam("file") MultipartFile file)
    {
        return AjaxResult.success("添加成功",wordInfoService.insertWord(word,file));
    }
    @RequiresPermissions("system:notice:list")
    @DeleteMapping("/delete")
    public AjaxResult deleteWord(@RequestBody String id){
        Integer result=wordInfoService.deleteWordById(id);
        if(result==0){
            return AjaxResult.error("不存在该记录");
        }
        else if (result==1){
            return AjaxResult.success("删除成功");
        }
        else {
            return AjaxResult.error("删除失败，没有权限");
        }
    }
}
