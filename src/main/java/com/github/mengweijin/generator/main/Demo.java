package com.github.mengweijin.generator.main;

import com.github.mengweijin.dto.ConfigParameter;
import com.github.mengweijin.dto.DbInfo;
import com.github.mengweijin.generator.enums.Template;

/**
 * @author mengweijin
 */
public class Demo {

    public static void main(String[] args) {
        ConfigParameter config = new ConfigParameter();
        config.setAuthor("Meng Wei Jin");
        config.setTemplateLocation(Template.MYBATIS_PLUS.getPath());
        config.setOutputPackage("src/main/java/com/github/mengweijin/testaaaaaaaaaaaaaaaaa");

        config.setTables(new String[]{"SYS_USER"});
        config.setTablePrefix("SYS_");

        config.setSuperEntityClass(null);
        config.setSuperDaoClass(null);
        config.setSuperServiceClass(null);
        config.setSuperServiceImplClass(null);
        config.setSuperControllerClass(null);
        config.setSuperEntityColumns(new String[]{"CREATE_BY", "CREATE_TIME", "UPDATE_BY", "UPDATE_TIME", "DELETED"});

        DbInfo dbInfo = new DbInfo();
        dbInfo.setUrl("jdbc:h2:file:C:/Source/code/gitee/quickboot/h2/test;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL");
        dbInfo.setDriverName("org.h2.Driver");
        dbInfo.setUsername("sa");
        dbInfo.setPassword("");
        config.setDbInfo(dbInfo);

        new CodeGenerator(config).run();
    }

}
