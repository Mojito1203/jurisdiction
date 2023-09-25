package com.situ.jurisdiction.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Mybatis自动填充
 */
@Component
public class MyObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
      metaObject.setValue("created", new Date());
      metaObject.setValue("updated",new Date());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updated",new Date());
    }
}
