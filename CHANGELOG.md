## CHANGELOG
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