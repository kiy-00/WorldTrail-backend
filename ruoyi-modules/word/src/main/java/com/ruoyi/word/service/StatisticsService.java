package com.ruoyi.word.service;

import com.ruoyi.word.entity.userWords.Log;
import com.ruoyi.word.entity.userWords.UserLexicon;
import com.ruoyi.word.repository.LexiconRepository;
import com.ruoyi.word.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LexiconRepository lexiconRepository;

    public StatisticsService(LogRepository logRepository, LexiconRepository lexiconRepository) {
        this.logRepository = logRepository;
        this.lexiconRepository = lexiconRepository;
    }

    public Log addLog(Log log) {

        return logRepository.save(log);  // 保存单个文档
    }

    @Transactional
    public void updateLexicon(Long userId, String lexiconId, String wordId, LocalDate data) {
        Optional<UserLexicon> optionalUserLexicon = lexiconRepository
                .findByUserIdAndLexiconIdAndWordId(userId, lexiconId, wordId);

        UserLexicon userLexicon = optionalUserLexicon.get();
        int newCount = userLexicon.getCount() + 1;
        userLexicon.setCount(newCount);
        userLexicon.setLastLearnTime(data);
        lexiconRepository.save(userLexicon);
    }

    public List<Map<String, Object>> getWeeklyStatistics(Long userId) {
        // 获取当前日期和一周前的日期
        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusDays(6);

        // 查询日志表获取过去一周的操作记录
        List<Log> logs = logRepository.findByUserIdAndReviewDateBetween(userId, oneWeekAgo, today);

        // 从用户词书表获取所有用户的词书记录
        Map<String, Integer> wordReviewCounts = lexiconRepository
                .findByUserId(userId)
                .stream()
                .collect(Collectors.toMap(
                        record -> record.getWordId(),
                        record -> record.getReviewCount(),
                        (existing, replacement) -> existing + replacement
                ));

        // 按日期分组日志记录
        Map<LocalDate, List<Log>> groupedLogs = logs.stream()
                .collect(Collectors.groupingBy(log -> log.getDate()));

        // 构造每日统计结果
        List<Map<String, Object>> result = new ArrayList<>();
        for (LocalDate date = oneWeekAgo; !date.isAfter(today); date = date.plusDays(1)) {
            List<Log> dailyLogs = groupedLogs.getOrDefault(date, new ArrayList<>());

            // 统计当天的 `reviewCount` 数据
            int reviewCount1 = 0;
            int reviewCountGreater1 = 0;

            for (Log log : dailyLogs) {
                int reviewCount = wordReviewCounts.getOrDefault(log.getWordId(), 0);
                if (reviewCount == 1) {
                    reviewCount1++;
                } else if (reviewCount > 1) {
                    reviewCountGreater1++;
                }
            }

            // 构造当天的统计信息
            Map<String, Object> dailyStats = new HashMap<>();
            dailyStats.put("date", date.toString());
            dailyStats.put("reviewCount1", reviewCount1);
            dailyStats.put("reviewCountGreater1", reviewCountGreater1);

            result.add(dailyStats);
        }

        return result;
    }
}

