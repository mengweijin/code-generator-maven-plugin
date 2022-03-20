## CHANGELOG
### [v1.0.12] 2022-03-20
- 【Library update】upgrade mybatis-plus to 3.5.1.
- 【Library update】upgrade mybatis-plus-generator to 3.5.2.
- 【Library update】upgrade beetl to 3.10.0.RELEASE.
- 【Library update】upgrade hutool to 5.7.22.
- 【Library update】upgrade fastjson to 1.2.79.
- 【Library update】upgrade snakeyaml to 1.30.
- 【Library update】upgrade velocity to 2.3.
- 【Library update】upgrade freemarker to 2.3.31.
- 【Library update】upgrade maven-core to 3.8.4.
- 【Library update】upgrade maven-plugin-api to 3.8.4.
- 【Library update】upgrade maven-plugin-annotations to 3.6.4.
- 【Library update】upgrade lombok to 1.18.22.

### [v1.0.11] 2021-12-26
- 【TEMPLATE UPDATE】Removed generate page search in controller.

### [v1.0.10] 2021-12-19
- 【Bug fixed】update jpa, mybatis, mybatis-plus templates.
- 【Bug fixed】entity 中没有导入包 superEntityClassPackage 的问题。

### [v1.0.9] 2021-11-21
- Add Comments in docker build script.

### [v1.0.8] 2021-10-01
- 【Bug fixed】当没有配置 superEntityClass 参数时出现的 NullPointerException。
- 【Bug fixed】生成 mybatis 的 service 类时，当没有配置 superServiceClass 参数，依然会在方法上面生成 @override 注解。

### [v1.0.7] 2021-09-26
- 升级 mybatis-plus-generator 版本到 3.5.1；
- 升级 mybatis-plus 版本到 3.4.3.4；
- 升级 beetl 版本到 3.7.0.RELEASE；

### [v1.0.6] 2021-06-06
- Add docker build image function(On localhost docker registry.).
- Add docker deploy container function(On localhost docker registry.).
- Add docker delete image and container function(On localhost docker registry.).

### [v1.0.5] 2021-06-03
- 升级 Hutool 版本从 5.6.3 到 5.6.5；
- 升级 mybatis-plus-generator 版本从 3.5.0.1-SNAPSHOT 到 3.5.0，版本兼容性处理；
- 添加生成 Dockerfile 文件，以及生成构建 Docker 镜像的脚本；
- 若干优化。

### [v1.0.4] 2021-04-11
- 升级 Hutool 版本从 5.6.2 到 5.6.3；
- Remove DriverUtils.java

### [v1.0.3] 2021-04-03
- 升级 mybatis-plus-generator 版本 从 3.4.1 到 3.5.0.1-SNAPSHOT
- fix 数据库驱动包在 Maven 中配置为 <scope>runtime</scope> 时找不到驱动包；
- Refactor classes in config package.
- Remove override class src/main/java/com/baomidou/mybatisplus/generator/config/querys/H2Query.java

### [v1.0.2] 2021-03-14
- 升级 beetl 版本从 3.3.1.RELEASE 到 3.3.2.RELEASE
- 移除自定义覆盖类：src/main/java/org/beetl/core/Configuration.java
- 升级 hutool 版本从 5.5.2 到 5.6.0
- Override class src/main/java/com/baomidou/mybatisplus/generator/config/querys/H2Query.java

### [v1.0.1] 2020-12-27
- Released first version.