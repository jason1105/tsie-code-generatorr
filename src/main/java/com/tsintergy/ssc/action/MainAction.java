package com.tsintergy.ssc.action;

import com.tsintergy.ssc.config.ConfigService;
import com.tsintergy.ssc.ui.win.Main;
import com.tsintergy.ssc.util.PluginUtils;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;

/**
 * 操作入口
 *
 */
public class MainAction extends AnAction {

    /**
     * 在 Database 面板中右键打开插件主页面
     *
     * @param actionEvent 操作对象
     */
    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        PsiElement[] psiElements = actionEvent.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        if (psiElements == null || psiElements.length == 0) {
            Messages.showWarningDialog("请至少选择一张表", "通知");
            return;
        }
        boolean isOk = false;
        for (PsiElement psiElement : psiElements) {
            if (psiElement instanceof DbTable) {
                isOk = true;
                break;
            }
        }
        if (!isOk) {
            Messages.showWarningDialog("请至少选择一张表", "通知");
            return;
        }
        Project project = actionEvent.getData(PlatformDataKeys.PROJECT);
        if (project == null) {
            Messages.showErrorDialog("无法获取到当前项目的 Project 对象", "错误");
            return;
        }
        PluginUtils.setProject(project);
        PluginUtils.syncResources();
        ConfigService configService = ConfigService.getInstance(project);
        if (configService == null) {
            Messages.showWarningDialog("初始化配置信息失败，但并不影响继续使用！", "错误");
            configService = new ConfigService();
        }
        new Main(actionEvent.getData(LangDataKeys.PSI_ELEMENT_ARRAY), configService);
    }
}
