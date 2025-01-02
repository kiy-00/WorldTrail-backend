package com.ruoyi.word.repository;

import com.ruoyi.word.entity.userWords.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LogRepository extends MongoRepository<Log, String> {

    // 查询用户在指定日期范围内的日志记录
    @Query("SELECT l FROM Log l WHERE l.userId = ':'userId AND l.reviewDate BETWEEN ':'startDate AND ':'endDate")
    List<Log> findByUserIdAndReviewDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
