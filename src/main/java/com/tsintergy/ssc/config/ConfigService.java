package com.tsintergy.ssc.config;

import cn.hutool.core.bean.BeanUtil;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.tsintergy.ssc.util.PluginUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * 配置 Service
 */
@Data
@State(name = "com.tsintergy.ssc.config.ConfigService", defaultStateAsResource = true, storages = {
    @Storage("database" + "-generator-config.xml")})
public class ConfigService implements PersistentStateComponent<ConfigService> {
    private Developer developer;
    private Options options;
    private Settings settings;

    /**
     * Node paths of selected nodes
     */
    private Set<String> nodePaths;

    public ConfigService() {
        ConfigVo configVo = PluginUtils.loadConfig();
        developer = configVo.getDeveloper();
        options = configVo.getOptions();
        settings = configVo.getSettings();
        nodePaths = new HashSet<>();
        assert developer != null;
        assert options != null;
        assert settings != null;

        checkProjectPath();
    }

    public synchronized void refresh() {
        ConfigVo configVo = PluginUtils.loadConfig();
        Developer _developer = configVo.getDeveloper();
        Options _options = configVo.getOptions();
        Settings _settings = configVo.getSettings();
        assert _developer != null;
        assert _options != null;
        assert _settings != null;
        BeanUtil.copyProperties(_developer, this.developer);
        BeanUtil.copyProperties(_options, this.options);
        BeanUtil.copyProperties(_settings, this.settings);

        checkProjectPath();
    }

    private void checkProjectPath() {
        if (StringUtils.isBlank(this.settings.getProjectPath())) {
            this.settings.setProjectPath(PluginUtils.getProject().getBasePath());
        }
    }

    @Nullable
    public static ConfigService getInstance(Project project) {
        return project.getService(ConfigService.class);
    }

    @Nullable
    @Override
    public ConfigService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ConfigService state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
