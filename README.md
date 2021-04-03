# code-generator-maven-plugin

Language: [中文](README.zh.md)

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

## Description
The code-generator-maven-plugin is an MVC project that generates Controller, Service, Entity, Dao(Mybatis: Mapper; JPA: Repository) layer of CRUD code maven plugin.
Implementation is based on Baomidou's mybatis-plus-generator.

Supported frameworks：Mybatis, Mybatis-Plus, JPA。

Theory can be extended to any background and front database table related technology: such as: vue.js.

The theory supports all databases that support JDBC connection: for example: DB2, DM, H2, Mariadb, MySQL, Oracle, Postgre, Sqlite, SQLServer, etc.

## how to use?
### 1. General Use
In the standard SpringBoot project, take Intellij IDEA, a development tool, as an example: the code-generator-maven-plugin was introduced into Maven
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
Locate the code-generator-maven-plugin in the Intellij IDEA Maven module shown below and double-click the corresponding plug-in command.

![image](docs/image/code-generator-maven-plugin.png)

#### Notes
* The default code generation is under the target/code-generator/ directory of the current project.
* The default package path is com.github.mengweijin.

### 2. Full Configuration to Use
~~~~xml
<plugin>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>code-generator-maven-plugin</artifactId>
    <version>Latest Version</version>
    <configuration>
        <parameters>
            <!--The package path that the code generates.               Default：com.github.mengweijin-->
            <outputPackage>com.github.mengweijin</outputPackage>
            <!--The value of @author above the class comment.           Default：Gets the user name of the current computer.-->
            <author>mengweijin</author>
            <!--Database connection information. 
            If it is a standard SpringBoot engineering, can be omitted, it will automatically read application.Yml/yaml/properties file.-->
            <dbInfo>
                <username>root</username>
                <password>root</password>
                <url>jdbc:mysql://192.168.83.128:3306/mwj_cms</url>
            </dbInfo>
            <!--The database table name to generate the code for. If not configured, all the tables in the database will be generated.
                Some databases are case-sensitive to table names, and the table names that need to be configured are exactly the same as those in the database.
                Multiple table names are separated by English commas.-->
            <tables>sys_user, rlt_user_role</tables>
            <!--To generate a prefix for the database table name corresponding to the code. 
                Once configured, the generated Entity class is not prefixed with a table. Such as：User, UserRole.
                If not configured, the generated Entity class is prefixed with a table. Such as：SysUser, RltUserRole.
                Multiple table name prefixes are separated by English commas.-->
            <tablePrefix>sys_, rlt_</tablePrefix>
            <!--The parent class inherited by the generated Entity class.-->
            <superEntityClass>com.github.mengweijin.BaseEntity</superEntityClass>
            <!--Whether the generated Entity is lombok mode enabled.
                Not configured or configured to true: Lombok enabled;
                Configured as false: Without Lombok mode enabled, the generated Entity contains getter/setter/toString methods.
            -->
            <lombokModel>false</lombokModel>

        </parameters>
    </configuration>
</plugin>
~~~~

## Parameter configuration instructions
|Parameter Name|Mandatory|Sample|Description|
|---:|:---|:---|:---|
|outputPackage|No|com.github.mengweijin|The package path to code generation. The default: com.github.mengweijin|
|author|No|mengweijin|Class annotation with the @Author value above. Default: take the user name of the current computer|
|dbInfo.username|No|root|The database connection information. If it is a standard SpringBoot project, can be omitted, automatically read application.yml/yaml/properties file.|
|dbInfo.password|No|root|Same as above|
|dbInfo.url|No|jdbc:mysql://192.168.83.128:3306/test|Same as above|
|tables|No|sys_user, rlt_user_role|Same as above|
|tablePrefix|No|sys_, rlt_|The prefix of the database table name corresponding to the code to be generated. Once configured, the generated Entity class will no longer have a table prefix. For example, User, UserRole. If not configured, the generated Entity class is prefixed with a table. For example, SysUser, RltUserRole. Multiple table name prefixes are separated by commas.|
|superEntityClass|No|com.github.mengweijin.quickboot.mybatis.BaseEntity|The generated Entity class inherits the parent class.|
|lombokModel|No|true|Whether the generated Entity is Lombok enabled. Unconfigured or true: Enable Lombok mode; Set to false: If Lombok is not enabled, the generated entity contains getter/setter/toString methods.|

## FAQ
1. The database table exists, but no code file is generated, and the program does not report an error.
  * Configure database table names to be exactly the same as table names in the database.
  For example, when an H2 database creates a table with a script, the script name is written in lowercase,
  but the generated table name may be in upper case, so you need to configure the upper case table name here.

## Futures
You are welcome to suggest better ways to improve this widget.
## Contributions
You are welcome to contribute code, let more people more time to accompany the people you care about.
