package com.tsintergy.ssc.task;

import com.tsintergy.ssc.util.FileUtils;
import com.tsintergy.ssc.util.Generator;
import com.tsintergy.ssc.util.PluginUtils;
import com.tsintergy.ssc.vo.impl.RootModel;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 */
public class GeneratorTask extends Task.Modal {
    private final Project project;
    private final JFrame windows;
    private final Generator generator;
    private final List<File> templates;
    private final List<RootModel> rootModels;

    public GeneratorTask(@NotNull Project project, JFrame windows, Generator generator, List<File> templates, List<RootModel> rootModels) {
        super(project, "生成代码", false);
        this.project = project;
        this.windows = windows;
        this.generator = generator;
        this.templates = templates;
        this.rootModels = rootModels;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setIndeterminate(false);

        generator(indicator);
    }

    public void generator(ProgressIndicator indicator) {
        indicator.setText("正在生成代码 ......");

        indicator.setFraction(0.0);

        int modelSize = rootModels.size();
        int templateSize = templates.size();

        int countInt = modelSize * templateSize;
        double count = (countInt) * 1.0;
        for (int i = 0; i < modelSize; i++) {
            RootModel rootModel = rootModels.get(i);
            int start = i * templateSize;
            generator.generator(rootModel, templates, (integer, filename) -> {
                int index = start + integer + 1;
                indicator.setText2(String.format("[%s/%s] [%s] --> %s",
                        index, countInt, rootModel.getTable().getName(), filename));
                indicator.setFraction(index / count);
            });
        }
        indicator.setText("生成代码完毕！");
        indicator.setText2("正在执行收尾工作，请稍候 ......");

        indicator.setFraction(1.0);
        indicator.setIndeterminate(true);
        PluginUtils.refreshProject();
    }

    @Override
    public void onSuccess() {
        super.onSuccess();
        windows.dispose();
        DumbService dumbService = DumbService.getInstance(project);
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification("正在等待其他任务完成后才能进行格式化代码操作 ...");
        }
        dumbService.smartInvokeLater(() -> {
            List<PsiFile> saveFiles = generator.getSaveFiles();
            if (!saveFiles.isEmpty()) {
                FileUtils.getInstance().reformatCode(project, saveFiles.toArray(new PsiFile[0]));
            }
        });
        Messages.showInfoMessage(String.format("代码构建完毕，涉及 %s 个数据库表和 %s 个模板文件，总共生成 %s 个文件。", rootModels.size(), templates.size(), generator.getSaveFiles().size()), "完成");
    }

    @Override
    public void onThrowable(@NotNull Throwable error) {
        super.onThrowable(error);
        Messages.showErrorDialog("代码生成失败:。\n\n" + error.getMessage(), "生成代码失败");
        windows.setVisible(true);
    }
}
