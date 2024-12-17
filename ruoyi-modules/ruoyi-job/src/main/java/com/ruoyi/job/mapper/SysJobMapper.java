package com.ruoyi.job.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.job.domain.SysJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 调度任务信息 数据层
 * 
 * @author ruoyi
 */
@Mapper
public interface SysJobMapper extends BaseMapper<SysJob>
{
    public List<SysJob> selectJobList(SysJob job);


    public int updateJob(SysJob job);

    public int insertJob(SysJob job);

}
