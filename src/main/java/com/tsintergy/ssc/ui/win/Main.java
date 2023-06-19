package com.tsintergy.ssc.ui.win;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.psi.PsiElement;
import com.tsintergy.ssc.config.ConfigService;
import com.tsintergy.ssc.config.Developer;
import com.tsintergy.ssc.config.Options;
import com.tsintergy.ssc.config.Settings;
import com.tsintergy.ssc.icon.DatabaseIcons;
import com.tsintergy.ssc.task.GeneratorTask;
import com.tsintergy.ssc.util.Generator;
import com.tsintergy.ssc.util.PluginUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.util.List;

public class Main extends JFrame {

    private static final Logger log = Logger.getInstance(Main.class);

    /**
     * 配置对象
     */
    private final transient ConfigService configService;
    /**
     * 配置对象：设置信息
     */
    private final transient Settings settings;
    /**
     * 配置对象：开发者信息
     */
    private final transient Developer developer;
    /**
     * 配置对象：参数信息
     */
    private final transient Options options;
    /**
     * 面板对象：数据库表配置
     */
    private final transient TableSetting tableSetting;
    /**
     * 面板对象：基础信息配置
     */
    private final transient BaseSetting baseSetting;
    /**
     * 面板对象：模板选择配置
     */
    private final transient SelectTemplate selectTemplate;
    /**
     * 输入框：项目路径输入、选择
     */
    private TextFieldWithBrowseButton projectPathField;
    /**
     * 输入框：Java代码路径
     */
    private JTextField javaPathField;
    /**
     * 输入框：资源代码路径
     */
    private JTextField sourcesPathField;

    /**
     * 输入框：输入内容监听
     */
    private final transient DocumentListener documentListener = new DocumentListener() {

        @Override
        public void insertUpdate(DocumentEvent e) {
            documentChanged(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            documentChanged(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        /**
         * swing 输入框组件内容更改事件
         *
         * @param e 事件
         */
        public void documentChanged(DocumentEvent e) {
            javax.swing.text.Document document = e.getDocument();
            if (!TextFieldDocumentUtil.updateSettingValue(document, javaPathField, settings::setJavaPath)
                && !TextFieldDocumentUtil.updateSettingValue(document, sourcesPathField, settings::setSourcesPath)) {
                TextFieldDocumentUtil.updateSettingValue(document, projectPathField.getTextField(), settings::setProjectPath);
            }
        }
    };
    /**
     * 内容Tab标签, 与 Main.form 中的 tableTabbedPane 对应
     */
    private JTabbedPane tableTabbedPane;
    /**
     * 按钮：完成按钮
     */
    private JButton finishButton;
    /**
     * 面板：内容面板, 与 Main.form 中的 content 对应
     */
    private JPanel content;
    private JButton resetButton;

    public Main(PsiElement[] psiElements, ConfigService configService) {
        super("代码生成器");
        this.configService = configService;
        this.settings = configService.getSettings();
        this.developer = configService.getDeveloper();
        this.options = configService.getOptions();

        this.javaPathField.setText(settings.getJavaPath());
        this.sourcesPathField.setText(settings.getSourcesPath());
        this.projectPathField.setText(settings.getProjectPath());


        baseSetting = new BaseSetting(settings, developer, options);
        selectTemplate = new SelectTemplate();
        tableSetting = new TableSetting(psiElements);
        tableTabbedPane.addTab("基础配置", baseSetting.getContent());
        tableTabbedPane.addTab("模板选择", selectTemplate.getContent());
        tableTabbedPane.addTab("数据库表配置", tableSetting.getContent());
        initWindows();
        initEventHandler();
    }

    /**
     * 初始化窗口配置
     */
    private void initWindows() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setContentPane(content);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        int frameWidth = content.getPreferredSize().width;
        int frameHeight = content.getPreferredSize().height;
        setLocation((screenSize.width - frameWidth) / 2, (screenSize.height - frameHeight) / 2);
        setResizable(false);
        pack();
        setVisible(true);
        resetButton.setEnabled(true);
        resetButton.setIcon(DatabaseIcons.REFRESH);
    }

    /**
     * 初始化配置信息
     */
    private void initEventHandler() {
        addListenerFor3Path();
        Project project = PluginUtils.getProject();

        FileChooserDescriptor chooserDescriptor = FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor();
        chooserDescriptor.setTitle("选择项目路径");

        projectPathField.addBrowseFolderListener(new TextBrowseFolderListener(chooserDescriptor, project));

        resetButton.addActionListener(event -> {
            JButton jButton = (JButton) event.getSource();
            Project project1 = PluginUtils.getProject();

            jButton.setEnabled(false);
            ConfigService _configService = ConfigService.getInstance(project1);
            if (_configService != null) {
                _configService.refresh(); // 从配置文件 config.yml 中读取配置信息 (即重置配置)
                reset3Path();
                baseSetting.initConfig();
            }
            jButton.setEnabled(true);
            resetButton.setIcon(DatabaseIcons.REFRESH);
        });
        // 点击完成按钮 默认
        finishButton.addActionListener(event -> {
            try {
                List<File> allSelectFile = selectTemplate.getAllSelectFile();
                if (allSelectFile.isEmpty()) {
                    Messages.showWarningDialog("当前无选中代码模板文件，无法进行代码生成，请选中至少一个代码模板文件！", "警告");
                    return;
                }
                setVisible(false);
                Generator generator = new Generator(settings, options, developer);
                GeneratorTask generatorTask = new GeneratorTask(project, this, generator, allSelectFile, tableSetting.getRootModels());
                ProgressManager.getInstance().run(generatorTask);
            } catch (Exception e) {
                e.printStackTrace();
                setVisible(true);
                Messages.showErrorDialog("初始化代码生成处理器失败，请联系开发者。\n\n" + e.getMessage(), "生成代码失败");
            }
            configService.setDeveloper(developer);
            configService.setOptions(options);
            configService.setSettings(settings);
        });
    }

    /**
     * 显示窗口
     */
    public void showWindows() {
        setAlwaysOnTop(true);
        setAlwaysOnTop(false);
    }

    /**
     * 初始化设置信息
     */
    private void addListenerFor3Path() {
        projectPathField.getTextField().getDocument().addDocumentListener(documentListener);
        javaPathField.getDocument().addDocumentListener(documentListener);
        sourcesPathField.getDocument().addDocumentListener(documentListener);
    }

    private void reset3Path() {
        projectPathField.setText(settings.getProjectPath());
        javaPathField.setText(settings.getJavaPath());
        sourcesPathField.setText(settings.getSourcesPath());
    }
}
