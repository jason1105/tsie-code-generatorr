package com.tsintergy.ssc.vo.impl;

import com.tsintergy.ssc.config.Settings;
import com.tsintergy.ssc.vo.IEntity;
import com.intellij.database.psi.DbTable;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.Set;

/**
 * 实体类信息
 *
 */
@Getter
public class EntityImpl implements IEntity {
    private final EntityPackage packages = new EntityPackage();
    private EntityName name;
    @Setter
    private String comment;
    @Setter
    private String uri;

    public EntityImpl(DbTable dbTable) {
        this.comment = StringUtils.defaultString(dbTable.getComment(), "");
        this.name = new EntityName(dbTable);
    }

    public void setName(String name) {
        this.name = new EntityName(name);
    }

    /**
     * 初始化更多的信息
     *
     * @param fullTypeNames 字段类型名称列表
     * @param settings      设置信息对象（用来初始化包名信息）
     */
    public void initMore(Set<String> fullTypeNames, Settings settings) {
        packages.clear();
        fullTypeNames.forEach(packages::add);
        name.initMore(settings);
        packages.initMore(settings, name);
    }
}
