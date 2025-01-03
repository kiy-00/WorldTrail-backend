package com.ruoyi.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.forum.entities.Vote;
import org.apache.ibatis.annotations.Mapper;
// 投票 Mapper 接口
@Mapper
public interface VoteMapper extends BaseMapper<Vote> {
}
