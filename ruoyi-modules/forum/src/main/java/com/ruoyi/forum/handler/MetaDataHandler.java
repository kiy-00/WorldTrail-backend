package com.ruoyi.forum.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
// 元数据处理器，用于自动填充 createTime 和 updateTime 字段
@Component
public class MetaDataHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动填充 createTime 和 updateTime 字段
        this.strictInsertFill(metaObject, "createdTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updatedTime", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 自动填充 updateTime 字段
        this.strictUpdateFill(metaObject, "updatedTime", Date.class, new Date());
    }
}
