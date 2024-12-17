package com.ruoyi.word.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.constant.Constants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.word.mapper.UserCheckinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.ruoyi.word.entity.users.UserCheckin;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Service
public class UserService {


    @Autowired
    private UserCheckinMapper mapper;



    // 每日签到
    public Short dailyCheckin() {
        Long uid=SecurityUtils.getLoginUser().getUserid();
        UserCheckin userCheckin=mapper.selectOne(new LambdaQueryWrapper<UserCheckin>()
                .eq(UserCheckin::getUser_id,uid).orderByDesc(UserCheckin::getId).last("limit 1"));
        if(userCheckin==null){
            userCheckin=new UserCheckin();
            userCheckin.setUser_id(uid);
            userCheckin.setCheckinDays((short)1);
            mapper.insert(userCheckin);
            return 1;
        }
        if(DateUtils.isSameDay(userCheckin.getUpdateTime(),new Date())){
            throw new ServiceException("今日已签到");
        }
        if(DateUtils.isSameDay(userCheckin.getUpdateTime(),DateUtils.addDays(new Date(),-1))){
            userCheckin.setCheckinDays((short)(userCheckin.getCheckinDays()+1));
            userCheckin.setUpdateTime(new Date());
            mapper.updateById(userCheckin);
        }
        else if(DateUtils.truncatedCompareTo(userCheckin.getUpdateTime(),new Date(),Calendar.DATE)<0){
            userCheckin.setId(null);
            userCheckin.setUser_id(uid);
            userCheckin.setCheckinDays((short)1);
            userCheckin.setUpdateTime(new Date());
            mapper.insert(userCheckin);
        }
        else {
            throw new ServiceException("签到异常");
        }
        return userCheckin.getCheckinDays();
    }

    // 获取连续签到天数
    public Short getCheckinStreak() {
        Long uid = SecurityUtils.getUserId();

        // 获取最新的签到记录
        UserCheckin userCheckin = mapper.selectOne(new LambdaQueryWrapper<UserCheckin>()
                .eq(UserCheckin::getUser_id, uid)
                .orderByDesc(UserCheckin::getId)
                .last("limit 1"));


        if(userCheckin == null || userCheckin.getUpdateTime().toInstant().atZone(ZoneId.of(Constants.ZONE_ID)).toLocalDate().isBefore(LocalDate.now().minusDays(1))) {
            return 0;
        }
        return userCheckin.getCheckinDays();
    }

}
