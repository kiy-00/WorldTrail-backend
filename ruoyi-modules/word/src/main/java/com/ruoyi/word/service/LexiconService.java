package com.ruoyi.word.service;

import com.ruoyi.word.entity.userWords.UserLexicon;
import com.ruoyi.word.entity.words.Book;
import com.ruoyi.word.repository.BookRepository;
import com.ruoyi.word.repository.LexiconRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LexiconService {

    private final BookRepository bookRepository;
    private final LexiconRepository lexiconRepository;

    @Autowired
    public LexiconService(BookRepository bookRepository, LexiconRepository lexiconRepository) {
        this.bookRepository = bookRepository;
        this.lexiconRepository = lexiconRepository;
    }

    public UserLexicon addUserLexicon(UserLexicon userLexicon) {
        return lexiconRepository.save(userLexicon);  // 保存单个文档
    }

    public List<UserLexicon> getUserLexicon(Long userId, String lexiconId) {
        return lexiconRepository.findByUserIdAndLexiconId(userId, lexiconId);
    }

    // Service 方法中调用
    public void updateLastReview(Long userId, String wordId, String lexiconId, int newReviewedCount, LocalDate newReviewedTime) {
        // 使用复合属性查询记录
        UserLexicon userLexicon = lexiconRepository.findByUserIdAndLexiconIdAndWordId(userId, lexiconId, wordId)
                .orElseThrow(() -> new RuntimeException("没有找到对应的记录"));

// 获取主键 ID 用于后续更新
        String id = userLexicon.getId();

// 修改非主码属性
        userLexicon.setCount(newReviewedCount);
        userLexicon.setLastLearnTime(newReviewedTime);

// 使用主键 ID 更新记录
        lexiconRepository.save(userLexicon); // 如果你已经获取了实体，直接保存即可
    }

    Optional<UserLexicon> findByUserIdAndLexiconNameAndWordId(Long userId, String lexiconId, String wordId) {
        return lexiconRepository.findByUserIdAndLexiconIdAndWordId(userId, lexiconId, wordId);
    }

    public List<Map<String, Object>> getLexiconStatistics(Long userId) {
        List<UserLexicon> userBooks = lexiconRepository.findByUserId(userId);

        return userBooks.stream()
                .filter(userLexicon -> userLexicon.getLexiconId() != null)
                .collect(Collectors.groupingBy(UserLexicon::getLexiconId))
                .entrySet()
                .stream()
                .map(entry -> {
                    String lexiconId = entry.getKey();

                    String lexiconName=bookRepository.findById(lexiconId).getBookName();
                    List<UserLexicon> books = entry.getValue();

                    int totalWordCount = books.size(); // 总单词数量
                    int reviewedWordCount = (int) books.stream().filter(book -> book.getReviewCount() > 0).count(); // 背过的单词数量
                    double reviewedRatio = (totalWordCount == 0) ? 0.0 : (double) reviewedWordCount / totalWordCount; // 比值

                    String status = (reviewedRatio == 1.0) ? "已完成" : "未完成"; // 状态

                    // 构造结果 Map
                    Map<String, Object> result = new HashMap<>();
                    result.put("wordbookName", lexiconName);
                    result.put("totalWordCount", totalWordCount);
                    result.put("reviewedWordCount", reviewedWordCount);
                    result.put("status", status);

                    return result;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> getLexiconProgress(String lexiconId, Long userId) {
        // 查询指定用户和词书的所有单词数据
        List<UserLexicon> words = lexiconRepository.findByUserIdAndLexiconId(userId, lexiconId);

        // 统计各类单词数量
        int totalWordCount = words.size();
        int unknownWordCount = (int) words.stream().filter(word -> word.getReviewCount() == 0).count();
        int knownCount = (int) words.stream().filter(word -> word.getReviewCount() == 1).count();
        int reviewCount = (int) words.stream().filter(word -> word.getReviewCount() > 1).count();
        String lexiconName=bookRepository.findById(lexiconId).getBookName();
        // 构造结果
        Map<String, Object> result = new HashMap<>();
        result.put("lexiconName", lexiconName);
        result.put("totalWordCount", totalWordCount);
        result.put("unknownWordCount", unknownWordCount);
        result.put("knownCount", knownCount);
        result.put("reviewCount", reviewCount);

        return result;
    }

    public boolean addBookToUserLexicon(Long userId, String lexiconId) {
        // 从词书表中查找指定的词书

        Book bookOptional = bookRepository.findById(lexiconId);

        Set<String> wordIds = bookOptional.getWords();

        // 将词书中的单词插入到用户的词书表中
        for (String wordId : wordIds) {
            // 检查用户词书表中是否已经存在该记录
            Optional<UserLexicon> existingRecord = lexiconRepository.findByUserIdAndLexiconIdAndWordId(userId, lexiconId, wordId);
            if (!existingRecord.isPresent()) {
                UserLexicon userLexicon = new UserLexicon(userId, lexiconId, wordId, LocalDate.now(), 0);
                lexiconRepository.save(userLexicon);
            }
        }

        return true; // 返回成功
    }

    public boolean deleteBooksFromUserLexicon(Long userId, String bookId) {
        // 查询所有满足条件的记录
        List<UserLexicon> userLexicons = lexiconRepository.findByUserIdAndLexiconId(userId, bookId);

        if (!userLexicons.isEmpty()) {
            // 删除所有记录
            lexiconRepository.deleteAll(userLexicons);
            return true; // 删除成功
        } else {
            return false; // 没有记录匹配条件
        }
    }

    public List<Map<String, Object>> getWordCountsByUserAndLexicon(Long userId, String lexiconId) {
        // 查询数据库获取匹配记录
        List<UserLexicon> userLexicons = lexiconRepository.findByUserIdAndLexiconId(userId, lexiconId);

        // 将结果映射为包含 wordId 和 count 的列表
        return userLexicons.stream()
                .map(userLexicon -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("wordId", userLexicon.getWordId());
                    map.put("count", userLexicon.getCount());
                    return map;
                })
                .collect(Collectors.toList());
    }
}