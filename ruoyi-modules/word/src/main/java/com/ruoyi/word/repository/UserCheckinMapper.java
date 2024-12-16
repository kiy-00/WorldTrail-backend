package com.ruoyi.word.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.ruoyi.word.entity.users.UserCheckin;
import org.springframework.stereotype.Component;


@Mapper
public interface UserCheckinMapper extends BaseMapper<UserCheckin> {
}
