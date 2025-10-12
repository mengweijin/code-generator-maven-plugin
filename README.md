# code-generator-maven-plugin

Language: [English](README.md)

<p align="center">
    <a target="_blank" href="https://github.com/mengweijin/code-generator-maven-plugin">
		<img src="https://img.shields.io/badge/repo-Github-purple" />
	</a>
    <a target="_blank" href="https://gitee.com/mengweijin/code-generator-maven-plugin">
		<img src="https://img.shields.io/badge/repo-码云 Gitee-purple" />
	</a>
	<a target="_blank" href="https://central.sonatype.com/artifact/com.github.mengweijin/code-generator-maven-plugin/versions">
		<img src="https://img.shields.io/maven-central/v/com.github.mengweijin/code-generator-maven-plugin" />
	</a>
	<a target="_blank" href="https://github.com/mengweijin/code-generator-maven-plugin/blob/master/LICENSE">
		<img src="https://img.shields.io/badge/license-Apache2.0-blue.svg" />
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" />
	</a>
	<a target="_blank" href="https://gitee.com/mengweijin/code-generator-maven-plugin/stargazers">
		<img src="https://gitee.com/mengweijin/code-generator-maven-plugin/badge/star.svg?theme=dark" alt='gitee star'/>
	</a>
	<a target="_blank" href='https://github.com/mengweijin/code-generator-maven-plugin'>
		<img src="https://img.shields.io/github/stars/mengweijin/code-generator-maven-plugin.svg?style=social" alt="github star"/>
	</a>
</p>

## 简介
code-generator-maven-plugin 在 Maven 项目中生成代码的 Maven 插件。主要包括：
- code-generator:**code**：基于数据库表，自定义 velocity 代码模板，生成 CRUD 或前端代码;
- code-generator:**mybatis**：基于数据库表，使用插件默认 MyBatis 模板生成 CRUD 代码;
- code-generator:**mybatis-plus**：基于数据库表，使用插件默认 MyBatis-Plus 模板生成 CRUD 代码;
- code-generator:**jpa**：基于数据库表，使用插件默认 JPA 模板生成 CRUD 代码;
- code-generator:**script**：生成脚本。包括：Dockerfile, app.sh, app.bat 等脚本。

备注：
- code-generator:**code** 插件理论可以生成任意前后台跟数据库表有关系的代码：如：Vue, Element-UI 代码等。
- 理论支持所有支持JDBC连接的数据库：例如：DB2, DM, H2, Mariadb, MySQL, Oracle, Postgre, Sqlite, SQLServer

## 如何使用?

在标准 SpringBoot 项目，以开发工具 Intellij IDEA 为例：在 Maven 中引入 code-generator-maven-plugin 插件
~~~~xml
<plugin>
   <groupId>com.github.mengweijin</groupId>
   <artifactId>code-generator-maven-plugin</artifactId>
   <version>Latest Version</version>
</plugin>
~~~~ 

在Intellij IDEA 的 Maven 模块中找到下面图中的 code-generator 插件，然后双击对应的插件命令即可。

code-generator:**code** 插件需要在 maven 运行窗口根据提示输入数据库表名称，模块名称。并且执行前需要指定用户自定义模板的位置，参数参考文章下面的表格。

![image](docs/image/code-generator-maven-plugin.png)

代码生成位置：在当前工程的 target/code-generator/ 目录下。

至此，初步使用完成。

## 全部配置使用（以 code-generator:code 插件为例）
~~~~xml
<plugin>
   <groupId>com.github.mengweijin</groupId>
   <artifactId>code-generator-maven-plugin</artifactId>
   <version>Latest Version</version>
   <configuration>
      <config>
         <baseEntity>com.github.mengweijin.vitality.framework.mybatis.entity.BaseEntity</baseEntity>
         <templateDir>generator/vue</templateDir> 
         <tablePrefix>vtl_</tablePrefix>
         <author>mengweijin</author>
         <dbInfo>
            <username>root</username>
            <password>root</password>
            <url>jdbc:mysql://localhost:3306/vitality</url>
         </dbInfo>
      </config>
   </configuration>
</plugin>
~~~~

## 参数配置说明
|            参数名称 | 是否必填 | 配置示例                                                               | 说明                                                                                                                      |
|----------------:|:-----|:-------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------|
|          author | 否    | mengweijin                                                         | 类注释上面@author的值。 默认：取当前电脑的用户名                                                                                            |
| dbInfo.username | 否    | root                                                               | 数据库连接信息。如果是标准的SpringBoot工程，可以省略，会自动读取application.yml/yaml/properties文件。                                                 |
| dbInfo.password | 否    | root                                                               | 同上                                                                                                                      |
|      dbInfo.url | 否    | jdbc:mysql://192.168.83.128:3306/test                              | 同上。                                                                                                                     |
|     tablePrefix | 否    | sys_, rlt_                                                         | 要生成代码对应的数据库表名称的前缀。配置后，生成的entity类就不会带有表前缀了。如：User, UserRole。如果不配置，生成的entity类就会带有表前缀。如：SysUser, RltUserRole。多个表名称前缀使用逗号分隔 |
|      baseEntity | 否    | com.github.mengweijin.vitality.framework.mybatis.entity.BaseEntity | 生成的entity类继承的父类                                                                                                         |
|     templateDir | 是    | generator/vue                                                      | 仅 code-generator:code 插件参数，用户自定义模板相对于项目根目录的位置。                                                                          |

## 常见问题
1. H2 数据库中表存在，但没有生成代码。
   * H2数据库默认区分表名称大小写，要么保证输入的表名称大小写完全一致，要么第一次创建 H2 数据库表时，jdbc url 参数增加 IGNORECASE=TRUE 参数。
2. 如何自定义生成代码的模板？
    * 使用 code-generator:code 插件，并在 pom.xml 中配置 templateDir 参数。
    * 模板文件参考 code-generator-maven-plugin 工程下，src\main\resources\templates 目录下的 velocity 模板 *.vm 文件。
    * 模板参数参考 ITemplateEngine.java 类中的 getObjectMap 方法或其他 .vm 中用到的参数。

## 期望
欢迎您提出更好的意见，帮助完善这个小插件.
## 贡献
欢迎您贡献代码，让更多的人多点时间陪陪关心的人。