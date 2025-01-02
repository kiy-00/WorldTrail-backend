package com.ruoyi.word.service;

import com.ruoyi.word.entity.userWords.UserLexicon;
import com.ruoyi.word.entity.words.Word;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserStudyPlanService {

    private final WordService wordService;//存储所有单词的表
    private LexiconService lexiconService;//存储用户和词书、单词对应关系的表
    private List<UserLexicon> wordsToReviewToday;
    private List<UserLexicon> wordsToLearnToday;
    private final int limit=10;

    @Autowired
    public UserStudyPlanService(LexiconService lexiconService,WordService wordService) {
        this.lexiconService = lexiconService;
        this.wordService = wordService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // 每天凌晨0点执行
    public void resetDailyWordLists() {
        wordsToReviewToday = null; // 或者 new ArrayList<>()
        wordsToLearnToday = null; // 或者 new ArrayList<>()
    }

    // 获取用户当天要复习的单词ID列表
    public void generateReviewWordsForToday(Long userId,String wordbookName) {
        //检索用户选择词书中的所有单词
        List<UserLexicon> allWords = lexiconService.getUserLexicon(userId,wordbookName);
        wordsToReviewToday = new ArrayList<>();//初始化待复习单词列表

        LocalDate now = LocalDate.now();
        for (UserLexicon word : allWords) {
            if (shouldReviewWord(now, word)) {
                wordsToReviewToday.add(word);
            }
        }
        System.out.println(wordsToReviewToday);
    }

    private boolean shouldReviewWord(LocalDate now, UserLexicon word) {
        // 使用 ChronoUnit.DAYS 来计算两个 LocalDate 之间的天数差异
        long daysBetween = ChronoUnit.DAYS.between(word.getLastLearnTime(), now);

        switch (word.getReviewCount()) {
            case 1:
                return daysBetween >= 1;
            case 2:
                return daysBetween >= 3;
            case 3:
                return daysBetween >= 7;
            case 4:
                return daysBetween >= 14;
            default:
                // 对于超过4次复习的单词，可以根据业务逻辑进行处理，这里假设不再需要复习
                return daysBetween >= 30;
        }
    }
    public int getReviewWordCount(Long userId,String wordbookName) {
        if (wordsToReviewToday == null|| wordsToReviewToday.isEmpty()) {
            generateReviewWordsForToday(userId,wordbookName);
        }
        return wordsToReviewToday.size();
    }


    private static final Logger logger = LoggerFactory.getLogger(UserStudyPlanService.class);
    public List<Word> getAGroupOfReviewWords(Long userId, String wordbookName) {
        if (wordsToReviewToday == null || wordsToReviewToday.isEmpty()) {
            generateReviewWordsForToday(userId, wordbookName);
            // 如果生成后仍然为空，则返回空列表
            if (wordsToReviewToday == null || wordsToReviewToday.isEmpty()) {
                return Collections.emptyList();
            }
        }

        // 存储一组复习单词的列表
        List<Word> AGroupOfReviewWords = new ArrayList<>();
        // 检查是否有足够的单词供选择
        int numWordsToPick = wordsToReviewToday.size();
        int i=0;
        logger.info("这里能跑通吗？");
        while(i<limit&&i<numWordsToPick){
            UserLexicon word = wordsToReviewToday.get(0);
            ObjectId objectId=new ObjectId(word.getWordId());
            Optional<Word> wordOptional = wordService.findById(objectId);
            // 添加这些日志
            logger.info("循环次数: {}, wordId: {}, wordOptional存在?: {}",
                    (i+1),
                    word.getWordId(),
                    wordOptional.isPresent()
            );

            if (wordOptional.isPresent()) {
                AGroupOfReviewWords.add(wordOptional.get());
                logger.info("成功添加单词到 AGroupOfReviewWords");
            } else {
                logger.warn("未找到对应的单词记录");
            }
            wordsToReviewToday.remove(0);
            logger.info("现在要更新表中的学习次数了。");
            updateWordStatus(userId,wordbookName,word);
            logger.info("更新成功了！");
            i++;
        }
        logger.info("现在要返回了。");
        return AGroupOfReviewWords;
    }
    //生成所有未学习单词列表
    public void generateToLearnWordsForToday(Long userId,String wordbookName) {
        //检索用户选择词书中的所有单词
        List<UserLexicon> allWords = lexiconService.getUserLexicon(userId,wordbookName);
        wordsToLearnToday = new ArrayList<>();//初始化待复习单词列表

        for (UserLexicon word : allWords) {
            if (word.getReviewCount()==0) {
                wordsToLearnToday.add(word);
            }
        }


    }

    public int getToLearnWordCount(Long userId,String wordbookName) {
        if (wordsToLearnToday == null|| wordsToLearnToday.isEmpty()) {
            generateToLearnWordsForToday(userId,wordbookName);
        }
        return wordsToLearnToday.size();
    }
    /**
     * 将 String 转换为 ObjectId，确保转换安全。
     *
     * @param id String 类型的 ID
     * @return Optional 包裹的 ObjectId；如果字符串非法，返回 Optional.empty()
     */
    public static Optional<ObjectId> toObjectId(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(new ObjectId(id));
        } catch (IllegalArgumentException e) {
            // 捕获非法 ObjectId 的情况
            return Optional.empty();
        }
    }
    //生成一组未学习单词
    public List<Word> getAGroupOfToLearnWords(Long userId,String wordbookName){
        if(wordsToLearnToday == null || wordsToLearnToday.isEmpty()){
            generateToLearnWordsForToday(userId,wordbookName);
            // 如果生成后仍然为空，则返回空列表
            if (wordsToLearnToday == null || wordsToLearnToday.isEmpty()) {
                return Collections.emptyList();
            }
        }
        List<Word> AGroupOfToLearnWords= new ArrayList<>();
        int numWordsToPick = wordsToLearnToday.size();
        System.out.println("numWordsToPick"+numWordsToPick);
        int i=0;
        while(i<limit&&i<numWordsToPick){
            UserLexicon word = wordsToLearnToday.get(0);
            ObjectId objectId=new ObjectId(word.getWordId());
            Optional<Word> wordOptional = wordService.findById(objectId);
            // 添加这些日志
            logger.info("循环次数: {}, wordId: {}, wordOptional存在?: {}",
                    (i+1),
                    word.getWordId(),
                    wordOptional.isPresent()
            );

            if (wordOptional.isPresent()) {
                AGroupOfToLearnWords.add(wordOptional.get());
                logger.info("成功添加单词到 AGroupOfToLearnWords");
            } else {
                logger.warn("未找到对应的单词记录");
            }
            wordsToLearnToday.remove(0);
            logger.info("现在要更新表中的学习次数了。");
            updateWordStatus(userId,wordbookName,word);
            logger.info("更新成功了！");
            i++;
        }
        System.out.println("AGroupOfToLearnWords.size()"+AGroupOfToLearnWords.size());
        return AGroupOfToLearnWords;
    }
    //更新已复习/学习单词的状态
    private void updateWordStatus(Long userId,String wordbookName, UserLexicon word) {

        String wordId = word.getWordId();
        LocalDate newReviewTime=LocalDate.now();
        int newReviewCount=word.getReviewCount()+1;

        //更新学习时间和学习次数
        lexiconService.updateLastReview(userId,wordId,wordbookName,newReviewCount,newReviewTime);
    }

    // 提供将指定单词的复习次数置为0的接口
    public void resetReviewCount(Long userId, String wordbookName,String wordId) {
        lexiconService.updateLastReview(userId,wordId,wordbookName,0,LocalDate.now());
    }

    // 提供让指定单词的复习次数-1的接口
    public void decrementReviewCount(Long userId, String wordbookName, String wordId) {
        // 查找 UserLexicon 记录
        Optional<UserLexicon> userLexiconOpt = lexiconService.findByUserIdAndLexiconNameAndWordId(userId, wordbookName, wordId);

        // 如果没有找到对应的记录，则抛出异常
        UserLexicon userLexicon = userLexiconOpt.orElseThrow(() ->
                new RuntimeException("没有找到对应的记录"));

        // 获取旧的学习次数
        int oldCount = userLexicon.getCount();

        // 检查学习次数是否大于0，防止出现负数
        if (oldCount > 0) {
            // 更新学习次数和最后复习时间
            lexiconService.updateLastReview(userId, wordId, wordbookName, oldCount - 1, LocalDate.now());
        } else {
            throw new RuntimeException("学习次数已经是0，无法减少");
        }
    }
}