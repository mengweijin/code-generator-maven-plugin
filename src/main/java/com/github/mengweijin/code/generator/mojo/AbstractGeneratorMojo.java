package com.github.mengweijin.code.generator.mojo;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.builder.GeneratorBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.github.mengweijin.code.generator.driver.DynamicDriverDataSource;
import com.github.mengweijin.code.generator.dto.Config;
import com.github.mengweijin.code.generator.dto.DbInfo;
import com.github.mengweijin.code.generator.engine.ITemplateEngine;
import com.github.mengweijin.code.generator.util.DbInfoUtils;
import com.github.mengweijin.code.generator.util.GeneratorUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.classloader.JarClassLoader;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author mengweijin
 */
@Slf4j
@Getter
public abstract class AbstractGeneratorMojo extends AbstractMojo {

    @Parameter
    private Config config;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(defaultValue = "${basedir}")
    private File baseDir;

    @Parameter(defaultValue = "${project.build.resources}", readonly = true, required = true)
    private List<Resource> resources;

    @Parameter(defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true)
    private File sourceDir;

    @Parameter(defaultValue = "${project.build.testResources}", readonly = true, required = true)
    private List<Resource> testResources;

    @Parameter(defaultValue = "${project.build.testSourceDirectory}", readonly = true, required = true)
    private File testSourceDir;

    @Parameter(defaultValue = "${project.compileClasspathElements}", readonly = true, required = true)
    private List<String> compilePath;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    protected abstract void checkTemplateExists() throws RuntimeException;

    protected abstract void initConfigTemplateDir();

    protected abstract ITemplateEngine getTemplateEngine();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            this.checkTemplateExists();

            this.initConfigTemplateDir();

            config.setPackages(project.getGroupId());

            String baseOutputDirPath = baseDir.getAbsolutePath() + "/target/code-generator/";
            String packagePath = StrUtil.replace(config.getPackages(), ".", "/");
            config.setOutputDir(baseOutputDirPath + packagePath);

            Set<String> allProjectRuntimeClasspathElements = this.getAllProjectRuntimeClasspathElements();
            this.loadProjectClassToContextClassLoader(allProjectRuntimeClasspathElements);

            if(config.getDbInfo() == null) {
                DbInfo dbInfo = DbInfoUtils.getDbInfo(config, allProjectRuntimeClasspathElements);
                config.setDbInfo(dbInfo);
            }
            if(config.getDbInfo() == null) {
                throw new RuntimeException("No datasource info was found! Please add database url, username, password configurations.");
            }

            Scanner sc = new Scanner(System.in);
            System.out.println("请输入数据库表名称（多个表名用逗号隔开），按 Enter 键继续：");
            String tableNames = sc.nextLine();
            while (StrUtil.isBlank(tableNames)) {
                System.out.println("请输入数据库表名称（多个表名用逗号隔开），按 Enter 键继续：");
                tableNames = sc.nextLine();
            }
            String[] tables = GeneratorUtils.trimItems(tableNames.split("[,，]"));

            System.out.println("请输入包模块名称（可以为空），按 Enter 键继续：");
            String moduleName = sc.nextLine();
            if(StrUtil.isNotBlank(moduleName)) {
                config.setModuleName(StrUtil.trim(moduleName.toLowerCase()));
                config.setPackages(config.getPackages() + "." + config.getModuleName());
                config.setOutputDir(config.getOutputDir() + "/" + config.getModuleName());
            }

            DynamicDriverDataSource dataSource = new DynamicDriverDataSource(config.getDbInfo().getUrl(), config.getDbInfo().getUsername(), config.getDbInfo().getPassword());
            DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(dataSource).build();

            StrategyConfig.Builder strategyConfigBuilder = GeneratorBuilder.strategyConfigBuilder();
            if(ArrayUtil.isNotEmpty(config.getTablePrefix())) {
                strategyConfigBuilder.addTablePrefix(config.getTablePrefix());
            }
            StrategyConfig strategyConfig = strategyConfigBuilder.addInclude(tables).build();

            // ConfigBuilder 类不能扩展，会报错。
            ConfigBuilder configBuilder = new ConfigBuilder(null, dataSourceConfig, strategyConfig, null, null, null);
            List<TableInfo> tableInfoList = configBuilder.getTableInfoList();

            System.out.println("----------------------------------------------------------------------------------------");
            System.out.println("主要输出配置信息：");
            System.out.println("数据库表：" + String.join(",", tables));
            System.out.println("数据库表前缀配置：" + (config.getTablePrefix() == null ? "" : String.join(",", config.getTablePrefix())));
            System.out.println("模板文件夹：" + config.getTemplateDir());
            System.out.println("包路径：" + config.getPackages());
            System.out.println("作者：" + config.getAuthor());
            System.out.println("实体父类：" + config.getBaseEntity());
            System.out.println("输出位置：" + config.getOutputDir());
            System.out.println("----------------------------------------------------------------------------------------");

            log.info("---------------------------------- 清理输出目录！----------------------------------");
            FileUtil.del(baseOutputDirPath);
            log.info("---------------------------------- 开始生成代码！----------------------------------");
            this.getTemplateEngine().init(config, tableInfoList).write();
            log.info("---------------------------------- 代码生成成功！----------------------------------");
            log.info("代码输出位置：{}", config.getOutputDir());
            log.info("---------------------------------- 代码生成完成！----------------------------------");
        } catch (Exception e) {
            getLog().error(e);
            throw new RuntimeException(e);
        }
    }

    private Set<String> getAllProjectRuntimeClasspathElements() {
        try {
            List<MavenProject> allProjects = session.getAllProjects();
            Set<String> runtimeElements = new HashSet<>();
            for (MavenProject project : allProjects) {
                runtimeElements.addAll(project.getRuntimeClasspathElements());
            }
            return runtimeElements;
        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void loadProjectClassToContextClassLoader(Set<String> allProjectRuntimeClasspathElements) {
        try {
            List<URL> urlList = new ArrayList<>();
            for (String element : allProjectRuntimeClasspathElements) {
                urlList.add(new File(element).toURI().toURL());
            }
            JarClassLoader jarClassLoader = new JarClassLoader(urlList.toArray(new URL[0]));
            Thread.currentThread().setContextClassLoader(jarClassLoader);
        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
