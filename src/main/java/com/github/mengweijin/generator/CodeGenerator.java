package com.github.mengweijin.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.github.mengweijin.generator.config.DefaultDataSourceConfig;
import com.github.mengweijin.generator.config.DefaultGlobalConfig;
import com.github.mengweijin.generator.config.DefaultInjectionConfig;
import com.github.mengweijin.generator.config.DefaultPackageConfig;
import com.github.mengweijin.generator.config.DefaultStrategyConfig;
import com.github.mengweijin.generator.config.DefaultTemplateConfig;
import com.github.mengweijin.generator.factory.TemplateEngineFactory;
import com.github.mengweijin.generator.util.FileOutConfigUtils;
import lombok.Data;
import org.apache.maven.model.Resource;

import java.io.File;
import java.util.List;

/**
 * @author mengweijin
 */
@Data
public class CodeGenerator {

    public static final String TMP_DIR = SystemUtil.get(SystemUtil.TMPDIR) + "code-generator/";

    private AutoGenerator autoGenerator;

    private Parameters parameters;

    private List<Resource> resourceList;

    private File baseDir;

    private File sourceDir;

    public void run() {
        autoGenerator = new AutoGenerator();
        // 全局配置
        autoGenerator.setGlobalConfig(new DefaultGlobalConfig(this));
        // 数据源配置
        autoGenerator.setDataSource(new DefaultDataSourceConfig(this));
        // 包配置
        autoGenerator.setPackageInfo(new DefaultPackageConfig(this));
        // Mybatis-plus自己的模板配置
        autoGenerator.setTemplate(new DefaultTemplateConfig(this));
        // 自定义配置, 会被优先输出
        InjectionConfig injectionConfig = new DefaultInjectionConfig(this);
        injectionConfig.setFileOutConfigList(FileOutConfigUtils.loadTemplatesToGetFileOutConfig(this));
        autoGenerator.setCfg(injectionConfig);
        // 策略配置
        autoGenerator.setStrategy(new DefaultStrategyConfig(this));
        // 模板引擎
        autoGenerator.setTemplateEngine(TemplateEngineFactory.getTemplateEngine(this.parameters.getTemplateType()));

        autoGenerator.execute();

        // clean TMP folder
        FileUtil.del(FileUtil.file(TMP_DIR));
    }
}
