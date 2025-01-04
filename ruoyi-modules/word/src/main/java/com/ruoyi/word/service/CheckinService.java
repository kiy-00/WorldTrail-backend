package com.ruoyi.word.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.constant.Constants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.word.mapper.UserCheckinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.word.entity.users.UserCheckin;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CheckinService {


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

    public List<Integer> getCheckinDates(Integer year, Integer month) {
        Long uid = SecurityUtils.getUserId();

        // 创建查询条件，查询该用户的打卡记录
        LambdaQueryWrapper<UserCheckin> query = new LambdaQueryWrapper<>();
        query.eq(UserCheckin::getUser_id, uid).orderByDesc(UserCheckin::getId);
        List<UserCheckin> userCheckins = mapper.selectList(query);

        // 用来存储该月的所有打卡日期
        Set<Integer> checkinDates = new TreeSet<>();

        // 遍历所有的打卡记录
        for (UserCheckin userCheckin : userCheckins) {
            // 获取打卡的最后一天
            LocalDate endDate = userCheckin.getUpdateTime().toInstant()
                    .atZone(ZoneId.of(Constants.ZONE_ID)) // 获取系统默认时区的 ZONE
                    .toLocalDate();

            // 获取打卡的开始日期
            LocalDate startDate = endDate.minusDays(userCheckin.getCheckinDays() - 1);
            if (endDate.isBefore(LocalDate.of(year, month, 1))) {
                continue;
            }
            LocalDate lastDayOfMonth = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
            if (startDate.isAfter(lastDayOfMonth)) {
                continue;
            }

            // 遍历该用户的打卡日期范围
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                // 如果日期属于该月，则加入到打卡日期集合
                if (currentDate.getYear() == year && currentDate.getMonthValue() == month) {
                    // 将日期格式化为yyyyMMdd并转换为Integer类型
                    checkinDates.add(currentDate.getDayOfMonth());
                }
                currentDate = currentDate.plusDays(1);

            }
        }

        // 返回该月的打卡日期列表
        return new ArrayList<>(checkinDates);
    }

}
