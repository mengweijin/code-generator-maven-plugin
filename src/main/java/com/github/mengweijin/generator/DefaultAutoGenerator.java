package com.github.mengweijin.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.github.mengweijin.generator.config.CustomerDataSource;
import com.github.mengweijin.generator.config.FileOutput;
import com.github.mengweijin.generator.entity.DbInfo;
import com.github.mengweijin.generator.entity.IdField;
import com.github.mengweijin.generator.entity.Parameters;
import com.github.mengweijin.generator.entity.ProjectInfo;
import com.github.mengweijin.generator.factory.TemplateEngineFactory;
import com.github.mengweijin.generator.util.DbInfoUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author mengweijin
 */
@Slf4j
public class DefaultAutoGenerator {

    @Getter
    private ProjectInfo projectInfo;

    public DefaultAutoGenerator(ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
    }

    public void execute() {
        Parameters parameters = projectInfo.getParameters();

        String outputDir = FileUtil.file(projectInfo.getBaseDir(), "target/code-generator/").getAbsolutePath();
        // clean directory target/code-generator
        FileUtil.del(outputDir);

        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(dataSourceConfigBuilder())
                .globalConfig(builder -> builder
                        .fileOverride()
                        .author(parameters.getAuthor())
                        .enableSwagger()
                        .disableOpenDir()
                        .outputDir(outputDir)
                        .dateType(DateType.TIME_PACK).commentDate("yyyy-MM-dd"))
                .packageConfig(builder -> builder.parent(parameters.getOutputPackage()))
                // 禁用所有默认模板，我们需要使用自定义的模板
                .templateConfig((Consumer<TemplateConfig.Builder>) TemplateConfig.Builder::disable)
                .strategyConfig(builder -> builder
                        .addInclude(this.trimItemName(parameters.getTables()))
                        .addTablePrefix(this.trimItemName(parameters.getTablePrefix()))
                        .entityBuilder()
                        .superClass(parameters.getSuperEntityClass())
                        //.disableSerialVersionUID()
                        .enableChainModel()
                        .enableLombok()
                        .enableTableFieldAnnotation()
                        // 乐观锁数据库列名
                        .versionColumnName("version")
                        // 乐观锁 Entity 属性名
                        .versionPropertyName("version")
                        // 逻辑删除数据库列名
                        .logicDeleteColumnName("deleted")
                        // 逻辑删除 Entity 属性名
                        .logicDeletePropertyName("deleted")
                        // 数据库表映射到实体的命名策略
                        .naming(NamingStrategy.underline_to_camel)
                        .addSuperEntityColumns(this.generateDefaultSuperEntityColumns())

                        .controllerBuilder()
                        .superClass(parameters.getSuperControllerClass())
                        // 开启驼峰转连字符
                        .enableHyphenStyle()
                        // 开启生成@RestController控制器
                        .enableRestStyle()

                        .serviceBuilder()
                        .superServiceClass(parameters.getSuperServiceClass())
                        .superServiceImplClass(parameters.getSuperServiceImplClass())

                        .mapperBuilder()
                        .superClass(parameters.getSuperDaoClass())
                        .enableBaseColumnList()
                        .enableBaseResultMap())
                .injectionConfig(builder -> {
                    builder
                            .beforeOutputFile(((tableInfo, objectMap) -> {
                                enhanceObjectMap(objectMap, parameters);
                                FileOutput.outputFile(tableInfo, objectMap, projectInfo, outputDir);
                            }))
                    //.customMap(Collections.singletonMap("test", "baomidou"))
                    //.customFile(Collections.singletonMap("test.txt", "/templates/test.vm"))
                    ;
                })
                .templateEngine(TemplateEngineFactory.getTemplateEngine(this.projectInfo.getParameters().getTemplateType()));


        fastAutoGenerator.execute();
    }

    private DataSourceConfig.Builder dataSourceConfigBuilder() {
        DbInfo dbInfo = DbInfoUtils.getDbInfo(projectInfo);
        // 自定义 CustomerDataSource, 使用自定义的 ClassLoader 加载类获取连接。
        CustomerDataSource dataSource = new CustomerDataSource(dbInfo.getUrl(), dbInfo.getUsername(), dbInfo.getPassword());
        return new DataSourceConfig.Builder(dataSource);
    }

    /**
     * If the user configured superEntityColumns, the configuration will prevail;
     * if not, the default configuration of superEntityColumns will be generated according to the superEntityClass.
     *
     * @return String[]
     */
    private String[] generateDefaultSuperEntityColumns() {
        String superEntityClass = projectInfo.getParameters().getSuperEntityClass();
        if(StrUtil.isBlank(superEntityClass)) {
            return new String[]{};
        }
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> cls = Class.forName(superEntityClass, true, classLoader);
            Field[] declaredFields = ClassUtil.getDeclaredFields(cls);
            return Arrays.stream(declaredFields)
                    .map(field -> StrUtil.toUnderlineCase(field.getName())).toArray(String[]::new);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String[] trimItemName(String[] items) {
        if (items == null) {
            return new String[]{};
        }
        return Arrays.stream(items).map(String::trim).toArray(String[]::new);
    }

    private static void enhanceObjectMap(Map<String, Object> objectMap, Parameters parameters) {
        objectMap.remove("package");
        objectMap.put("parameters", parameters);
        objectMap.put("idField", getIdField((TableInfo) objectMap.get("table")));
        objectMap.put("allFieldList", handleAllFieldList((TableInfo) objectMap.get("table")));

        log.info("Beetl parameter map: {}", objectMap);
    }

    private static IdField getIdField(TableInfo tableInfo) {
        TableField tableField = tableInfo.getFields().stream().filter(TableField::isKeyFlag).findFirst().orElse(null);

        IdField idField = new IdField();
        if(tableField != null) {
            idField.setColumnName(tableField.getName());
            idField.setPropertyName(tableField.getPropertyName());
            idField.setPropertyType(tableField.getColumnType().getType());
        }
        return idField;
    }

    private static List<TableField> handleAllFieldList(TableInfo tableInfo) {
        List<TableField> fieldList = tableInfo.getFields();
        List<TableField> commonFields = tableInfo.getCommonFields();

        // 为了不影响其他地方的引用，这里必须创建一个新的集合来存放所有字段
        List<TableField> allList = new ArrayList<>();
        allList.addAll(fieldList);
        allList.addAll(commonFields);
        return allList;
    }
}
