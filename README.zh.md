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
code-generator-maven-plugin 是一个在MVC项目中基于数据库表生成Controller, Service, entity, Dao(mybatis: Mapper; JPA: Repository)层CRUD代码的maven插件。
基于baomidou mybatis-plus-generator实现。

支持的框架：Mybatis, Mybatis-Plus, JPA。

理论可以扩展任意前后台跟数据库表有关系的技术：如：Vue.js 等。

理论支持所有支持JDBC连接的数据库：例如：DB2, DM, H2, Mariadb, MySQL, Oracle, Postgre, Sqlite, SQLServer

## 如何使用?
### 1. 入门使用
在标准 SpringBoot 项目，以开发工具 Intellij IDEA 为例：在 Maven 中引入 code-generator-maven-plugin 插件
~~~~xml
<plugin>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>code-generator-maven-plugin</artifactId>
    <version>Latest Version</version>
</plugin>
~~~~ 
在Intellij IDEA的Maven模块中找到下面图中的code-generator插件，然后双击对应的插件命令即可。

![image](docs/image/code-generator-maven-plugin.png)

#### 注意
* 默认代码生成在当前工程的target/code-generator/ 目录下。
* 默认包路径为：com.github.mengweijin

### 2. 一般使用
~~~~xml
<plugin>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>code-generator-maven-plugin</artifactId>
    <version>Latest Version</version>
    <configuration>
        <parameters>
            <tables>sys_user</tables>
            <tablePrefix>sys_</tablePrefix>
            <superEntityClass>com.github.mengweijin.quickboot.mybatis.BaseEntity</superEntityClass>
            <lombokModel>false</lombokModel>
            <dbInfo>
                <username>root</username>
                <password>root</password>
                <driverName>com.mysql.cj.jdbc.Driver</driverName>
                <url>jdbc:mysql://192.168.83.128:3306/mwj_cms</url>
            </dbInfo>
        </parameters>
    </configuration>
</plugin>
~~~~
### 3. 全部配置使用
~~~~xml
<plugin>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>code-generator-maven-plugin</artifactId>
    <version>Latest Version</version>
    <configuration>
        <parameters>
            <!--代码生成的包路径。               默认：com.github.mengweijin-->
            <outputPackage>com.github.mengweijin</outputPackage>
            <!--类注释上面@author的值。         默认：取当前电脑的用户名-->
            <author>mengweijin</author>
            <!--(当前版本暂不需要配置)自定义生成代码的模板文件，一般不需要配置。如配置，需要配置绝对路径的目录-->
            <!--<templateLocation>C:\\templates\</templateLocation>-->
            <!--(当前版本暂不需要配置)自定义生成代码的模板引擎的类型，一般不需要配置。如配置，支持 'beetl', 'velocity', 'freemarker'-->
            <!--<templateType>C:\\templates\</templateType>-->
            <!--数据库连接信息。如果是标准的SpringBoot工程，可以省略，会自动读取application.yml/yaml/properties文件。-->
            <dbInfo>
                <username>root</username>
                <password>root</password>
                <driverName>com.mysql.cj.jdbc.Driver</driverName>
                <url>jdbc:mysql://192.168.83.128:3306/mwj_cms</url>
            </dbInfo>
            <!--要生成代码对应的数据库表名称。如果不配置，会生成数据库中所有的表。
                部分数据库对表名称大小写敏感，此时需要配置的表名称跟数据库中的完全一致。
                多个表名称使用英文逗号分隔-->
            <tables>sys_user, rlt_user_role</tables>
            <!--要生成代码对应的数据库表名称的前缀。配置后，生成的entity类就不会带有表前缀了。如：User, UserRole。
                如果不配置，生成的entity类就会带有表前缀。如：SysUser, RltUserRole。
                多个表名称前缀使用英文逗号分隔-->
            <tablePrefix>sys_, rlt_</tablePrefix>
            <!--生成的entity类继承的父类-->
            <superEntityClass>com.github.mengweijin.BaseEntity</superEntityClass>
            <!--生成的entity类继承的父类BaseEntity中的公共字段。
                如果不配置，程序根据配置的superEntityClass取其属性，然后驼峰命名转为下划线作为数据库表的公共列名称。
                如果配置，则以配置的为准。
                因此，如果你的数据库表字段的命名规则和BaseEntity的命名规则也是驼峰转下划线的方式，就可以不配置这一项。
            -->
            <superEntityColumns>create_time, update_time, create_by, update_by</superEntityColumns>
            <!--生成的Controller类继承的父类-->
            <superControllerClass>com.github.mengweijin.BaseController</superControllerClass>
            <!--(当前版本暂不需要配置)生成的dao类继承的父类-->
            <!--<superDaoClass>com.github.mengweijin.BaseMapper</superDaoClass>-->
            <!--(当前版本暂不需要配置)生成的Service类继承的父类-->
            <!--<superServiceClass>com.github.mengweijin.BaseService</superServiceClass>-->
            <!--(当前版本暂不需要配置)生成的ServiceImpl类继承的父类-->
            <!--<superServiceImplClass>com.github.mengweijin.BaseServiceImpl</superServiceImplClass>-->
            <!--生成的entity是否启用lombok方式。
                不配置或者配置为true: 启用lombok方式；
                配置为false: 不启用lombok方式，则生成的entity中包含getter/setter/toString方法。
            -->
            <lombokModel>false</lombokModel>

        </parameters>
    </configuration>
</plugin>
~~~~
## 常见问题
~~1. 抛出类似异常：java.lang.ClassNotFoundException: org.h2.Driver，无法找到驱动类~~
~~    * 配置驱动类时，不能配置 maven 的 scope 节点的值为 runtime~~
````
<dependency>
     <groupId>com.h2database</groupId>
     <artifactId>h2</artifactId>
     <!--注释掉这一行，其他数据库驱动同理。 <scope>runtime</scope> -->
 </dependency>
````
2. 数据库表存在，但没有生成代码文件，程序也没有报错。
    * 配置数据库表名称（tables）一定要跟数据库中的表名称大小写完全一致。例如H2数据库用脚本创建表时的脚本中写的名称是小写，但真实生成的表名称可能是大写的，因此这里需要配置为大写的表名称。

## 期望
欢迎您提出更好的意见，帮助完善这个小插件.
## 贡献
欢迎您贡献代码，让更多的人多点时间陪陪关心的人。