package com.tsintergy.ssc.vo.impl;

import com.tsintergy.ssc.config.Settings;
import com.tsintergy.ssc.vo.IEntityField;
import com.tsintergy.ssc.vo.ITable;
import com.tsintergy.ssc.vo.ITableColumn;
import com.intellij.database.psi.DbTable;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 完整的类型信息
 *
 */
@Getter
public class RootModel {
    /**
     * 实体对象信息
     */
    private final EntityImpl entity;
    /**
     * 实体对象字段列表
     */
    private final List<? extends IEntityField> fields;
    /**
     * 数据库表信息
     */
    private final ITable table;
    /**
     * 数据库表字段列表
     */
    private final List<? extends ITableColumn> columns;

    private final PrimaryInfo primary;

    public RootModel(DbTable dbTable, List<EntityFieldImpl> fields, List<TableColumnImpl> columns) {
        this.table = new TableImpl(dbTable);
        this.entity = new EntityImpl(dbTable);
        this.fields = fields;
        this.columns = columns;
        this.primary = new PrimaryInfo(fields);
    }

    public EntityImpl getEntity(Settings settings) {
        Set<String> fullTypeNames = fields.stream().map(IEntityField::getFullTypeName).collect(Collectors.toSet());
        entity.initMore(fullTypeNames, settings);
        return entity;
    }
}
