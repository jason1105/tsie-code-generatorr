package com.tsintergy.ssc.vo.impl;

import com.tsintergy.ssc.vo.IName;
import com.google.common.base.CaseFormat;
import com.intellij.database.model.DasColumn;
import lombok.Getter;

/**
 * Java字段名称对象
 *
 */
@Getter
public class FieldNameInfo implements IName {
    private final String value;
    private final String firstUpper;
    private final String firstLower;

    public FieldNameInfo(String value, String firstUpper, String firstLower) {
        this.value = value;
        this.firstUpper = firstUpper;
        this.firstLower = firstLower;
    }

    public FieldNameInfo(DasColumn dbColumn) {
        this.value = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, dbColumn.getName());
        this.firstUpper = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, value);
        this.firstLower = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
