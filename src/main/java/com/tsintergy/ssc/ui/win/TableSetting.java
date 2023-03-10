package com.tsintergy.ssc.ui.win;

import com.tsintergy.ssc.vo.impl.RootModel;
import com.intellij.database.psi.DbTable;
import com.intellij.psi.PsiElement;
import lombok.Data;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表配置面板（涵盖多个数据库表的内容）
 *
 */
@Data
public class TableSetting implements IWindows {
    /**
     * 面板：顶级内容面板  bind to TableSetting.form
     */
    private JPanel content;
    /**
     * 标签：包含多个数据库表的标签界面 bind to TableSetting.form
     */
    private JTabbedPane tableTabbedPane;
    private PsiElement[] psiElements;
    private List<TablePanel> tablePanels = new ArrayList<>();

    /**
     * Construct a new TableSetting object.
     *
     * @param psiElements
     */
    public TableSetting(PsiElement[] psiElements) {
        this.psiElements = psiElements;
        for (PsiElement psiElement : psiElements) {
            if (psiElement instanceof DbTable) {
                DbTable dbTable = (DbTable) psiElement;
                TablePanel tablePanel = new TablePanel(dbTable);
                tableTabbedPane.addTab(dbTable.getName(), tablePanel.getContent());
                tablePanels.add(tablePanel);
            }
        }
    }

    /**
     * Export all tables that is saved in each tabbed panel separately
     *
     * @return
     */
    public List<RootModel> getRootModels() {
        List<RootModel> rootModels = new ArrayList<>();
        for (TablePanel tablePanel : tablePanels) {
            rootModels.add(tablePanel.toModel());
        }
        return rootModels;
    }

    @Override
    public JPanel getContent() {
        return content;
    }
}
