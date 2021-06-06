# code-generator-maven-plugin

Language: [English](README.md)

<p align="center">	
	<a target="_blank" href="https://search.maven.org/search?q=g:%22com.github.mengweijin%22%20AND%20a:%22code-generator-maven-plugin%22">
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
code-generator-maven-plugin 是一个基于baomidou mybatis-plus-generator实现的，在 Maven 项目中生成代码的 Maven 插件。主要包括：
- code-generator:**MyBatis**：基于数据库表生成 Mybatis 下的 Controller.java, Service.java, Mapper.java, mapper.xml, Entity.java 层CRUD代码;
- code-generator:**MyBatis-Plus**：基于数据库表生成 Mybatis-plus 下的 Controller.java, Service.java, Mapper.java, mapper.xml, Entity.java 层CRUD代码;
- code-generator:**JPA**：基于数据库表生成 Jpa 下的 Controller.java, Service.java, Repository.java, Entity.java 层CRUD代码;
- code-generator:**Dockerfile**：生成当前项目的 Dockerfile 文件，以及相关脚本：DockerImageBuild.bat, DockerImageBuildRun.bat, DockerImageDelete.bat
- code-generator:**Docker-Build**：基于本地安装的 Docker 来构建 docker image
- code-generator:**Docker-Deploy**：基于本地安装的 Docker 来构建 docker image 并部署 docker 容器
- code-generator:**Docker-Delete**：基于本地安装的 Docker 来删除已经部署的 docker 容器和 docker image
- 理论可以扩展任意前后台跟数据库表有关系的技术：如：Vue, Element-UI 代码等。
- 理论支持所有支持JDBC连接的数据库：例如：DB2, DM, H2, Mariadb, MySQL, Oracle, Postgre, Sqlite, SQLServer

## 如何使用?
在Intellij IDEA 的 Maven 模块中找到下面图中的 code-generator 插件，然后双击对应的插件命令即可。

![image](docs/image/code-generator-maven-plugin.png)

**注意**
* 默认 Java 代码生成在当前工程的 target/code-generator/ 目录下。
* 默认包路径为：com.github.mengweijin
* 默认 Dockerfile 等文件生成在当前工程的 target 目录下。

## 生成 java 代码
### 1. 一般使用
在标准 SpringBoot 项目，以开发工具 Intellij IDEA 为例：在 Maven 中引入 code-generator-maven-plugin 插件
~~~~xml
<plugin>
   <groupId>com.github.mengweijin</groupId>
   <artifactId>code-generator-maven-plugin</artifactId>
   <version>Latest Version</version>
   <configuration>
      <parameters>
         <tables>sys_user, sys_role</tables>
         <superEntityClass>com.github.mengweijin.quickboot.mybatis.BaseEntity</superEntityClass>
      </parameters>
   </configuration>
</plugin>
~~~~ 

### 2. 全部配置使用
~~~~xml
<plugin>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>code-generator-maven-plugin</artifactId>
    <version>Latest Version</version>
    <configuration>
        <parameters>
            <outputPackage>com.github.mengweijin</outputPackage>
            <author>mengweijin</author>
            <dbInfo>
                <username>root</username>
                <password>root</password>
                <url>jdbc:mysql://192.168.83.128:3306/mwj_cms</url>
            </dbInfo>
            <tables>sys_user, rlt_user_role</tables>
            <tablePrefix>sys_, rlt_</tablePrefix>
            <superEntityClass>com.github.mengweijin.BaseEntity</superEntityClass>
            <lombokModel>true</lombokModel>
        </parameters>
    </configuration>
</plugin>
~~~~

## 参数配置说明
|参数名称|是否必填|配置示例|说明|
|---:|:---|:---|:---|
|outputPackage|否|com.github.mengweijin|代码生成的包路径。 默认：com.github.mengweijin|
|author|否|mengweijin|类注释上面@author的值。 默认：取当前电脑的用户名|
|dbInfo.username|否|root|数据库连接信息。如果是标准的SpringBoot工程，可以省略，会自动读取application.yml/yaml/properties文件。|
|dbInfo.password|否|root|同上|
|dbInfo.url|否|jdbc:mysql://192.168.83.128:3306/test|同上。注意：如果是多模块项目使用 H2 数据库生成代码时，要注意 URL 的书写方式，详情参考**常见问题**章节|
|tables|否|sys_user, rlt_user_role|要生成代码对应的数据库表名称。如果不配置，会生成数据库中所有的表。部分数据库对表名称大小写敏感，此时需要配置的表名称跟数据库中的完全一致。多个表名称使用英文逗号分隔|
|tablePrefix|否|sys_, rlt_|要生成代码对应的数据库表名称的前缀。配置后，生成的entity类就不会带有表前缀了。如：User, UserRole。如果不配置，生成的entity类就会带有表前缀。如：SysUser, RltUserRole。多个表名称前缀使用英文逗号分隔|
|superEntityClass|否|com.github.mengweijin.quickboot.mybatis.BaseEntity|生成的entity类继承的父类|
|lombokModel|否|true|生成的entity是否启用lombok方式。不配置或者配置为true: 启用lombok方式；配置为false: 不启用lombok方式，则生成的entity中包含getter/setter/toString方法。|

## 常见问题
1. 数据库表存在，但没有生成代码文件，程序也没有报错。
   * 配置数据库表名称（tables）一定要跟数据库中的表名称大小写完全一致。例如H2数据库用脚本创建表时的脚本中写的名称是小写，但真实生成的表名称可能是大写的，因此这里需要配置为大写的表名称。
2. 多模块项目使用 H2 数据库生成代码时，提示“表\[table_name]在数据库中不存在！！！” 的问题。
   * 项目结构为多模块时，项目结构如下：
   ````txt
   - project-parent
      - h2
         - test.mv.db
      - project-child
         - src
            - main
               - java
               - resources
         - pom.xml(code-generator-maven-plugin 在这里配置的)
      - pom.xml(project-parent 的)
   ````
   * 可以发现，/h2/test.mv.db 文件在整个项目的根路径下 
   * 我们程序中配置的 url 为 jdbc:h2:file:./h2/test;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL
   * 当使用 code-generator-maven-plugin 时，就不能配置上面的 url 了， 因为多模块项目中的插件的根路径为 project-child 的，并不是 /h2/test.mv.db 文件所在的位置
   * 此时我们可以使用以下两种方式来手动指定：
      * 使用绝对路径：jdbc:h2:file:C:/Source/code/gitee/quickboot/h2/test;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL
      * 使用相对路径（多加了一层 ../ 符号）：jdbc:h2:file:./../h2/test;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL
   * 注意：只用多模块项目才需要这样指定，单个项目不受影响。
   

## 期望
欢迎您提出更好的意见，帮助完善这个小插件.
## 贡献
欢迎您贡献代码，让更多的人多点时间陪陪关心的人。