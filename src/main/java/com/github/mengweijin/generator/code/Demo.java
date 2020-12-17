package com.github.mengweijin.generator.code;

/**
 * @author mengweijin
 */
public class Demo {

    public static void main(String[] args) {
        // ConfigProperty中字段的值请参考类中的注释
        ConfigProperty config = new ConfigProperty();
        config.setAuthor("Meng Wei Jin");
        config.setTemplate(ETemplate.MYBATIS_PLUS.getPath());
        config.setOutputPackage("com.github.mengweijin.testaaaaaaaaaaaaaaaaa");

        config.setTables(new String[]{"SYS_USER"});
        config.setTablePrefix("SYS_");

        config.setSuperEntityClass(null);
        config.setSuperDaoClass(null);
        config.setSuperServiceClass(null);
        config.setSuperServiceImplClass(null);
        config.setSuperControllerClass(null);
        config.setSuperEntityColumns(new String[]{"CREATE_BY", "CREATE_TIME", "UPDATE_BY", "UPDATE_TIME", "DELETED"});

        config.setDbUrl("jdbc:h2:file:C:/Source/code/gitee/quickboot/h2/test;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL");
        config.setDbDriverName("org.h2.Driver");
        config.setDbUsername("sa");
        config.setDbPassword("");

        new CodeGenerator(config).run();
    }

}
