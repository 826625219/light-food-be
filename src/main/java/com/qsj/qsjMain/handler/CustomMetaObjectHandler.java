package com.qsj.qsjMain.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * metaObjectHandler for insert and update
 */

@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Date currDate = new Date();
        Object createTime = this.getFieldValByName("createTime", metaObject);
        Object updateTime = this.getFieldValByName("updateTime", metaObject);
        if (createTime == null) {
            this.setFieldValByName("createTime", currDate.getTime(), metaObject);
        }
        if (updateTime == null) {
            this.setFieldValByName("updateTime", currDate.getTime(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date currDate = new Date();
        Object updateTime = this.getFieldValByName("updateTime", metaObject);
        if (updateTime == null) {
            this.setFieldValByName("updateTime", currDate.getTime(), metaObject);
        }
    }
}
