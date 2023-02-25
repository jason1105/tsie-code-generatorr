package com.tsintergy.ssc.vo.impl;

import com.tsintergy.ssc.vo.IName;
import lombok.Getter;

/**
 * 实体类包信息
 *
 */
@Getter
public class EntityPackageInfo {
    /**
     * 实体类包名（不含实体类名称）
     */
    private final String pack;
    /**
     * 实体类完整包路径（含实体类名称）
     */
    private final String full;

    public EntityPackageInfo(String pack, IName entityName) {
        this.pack = pack;
        this.full = String.format("%s.%s", pack, entityName);
    }

    @Override
    public String toString() {
        return pack;
    }
}
