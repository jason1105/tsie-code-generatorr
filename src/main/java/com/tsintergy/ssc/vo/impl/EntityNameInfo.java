package com.tsintergy.ssc.vo.impl;

import com.tsintergy.ssc.vo.IName;
import com.google.common.base.CaseFormat;
import lombok.Getter;

/**
 * 实体类名称信息对象
 *
 */
@Getter
public class EntityNameInfo implements IName {
    private final String value;
    private final String firstUpper;
    private final String firstLower;

    public EntityNameInfo(String entityName, String suffix) {
        this.value = entityName + suffix;
        this.firstLower = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, toString());
        this.firstUpper = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
