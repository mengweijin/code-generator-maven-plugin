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
code-generator-maven-plugin is based on baomidou's mybatis-plus-generator，a Maven plugin that generates code in a Maven project。Key features：
- code-generator:**MyBatis**：Generate Controller.java, Service.java, Mapper.java, mapper.xml, Entity.java under MyBatis based on database tables;
- code-generator:**MyBatis-Plus**：Generate Controller.java, Service.java, Mapper.java, mapper.xml, Entity.java under MyBatis-Plus based on database tables;
- code-generator:**JPA**：Generate Controller.java, Service.java, Repository.java, Entity.java under JPA based on database tables;
- code-generator:**Customer**：Generate CRUD code based on database tables and specify custom template locations;
- code-generator:**Dockerfile**：Generate the Dockerfile file for the current project, along with the associated scripts: DockerImageBuild.bat, DockerImageBuildRun.bat, DockerImageDelete.bat
- code-generator:**Docker-Build**：Build Docker Image based on the locally installed Docker
- code-generator:**Docker-Deploy**：Build Docker image and deploy Docker container based on locally installed Docker
- code-generator:**Docker-Delete**：Delete deployed Docker containers and Docker images based on the locally installed Docker
- Theory can be extended to any background and front database table related technology: such as: vue.js.
- The theory supports all databases that support JDBC connection: for example: DB2, DM, H2, Mariadb, MySQL, Oracle, Postgre, Sqlite, SQLServer, etc.

### SpringBoot Code Generator
[generator-spring-boot-starter](https://gitee.com/mengweijin/vitality)

## how to use?
Locate the code-generator-maven-plugin in the Intellij IDEA Maven module shown below and double-click the corresponding plug-in command.

![image](docs/image/code-generator-maven-plugin.png)

**Notes**
* The default Java code generation is under the target/code-generator/ directory of the current project.
* The default package path is com.github.mengweijin.
* The default Dockerfile and other files are generated in the target directory of the current project.
* The Customer plug-in must be configured with templateLocation parameters. By default, the templateType parameter is beetl.

## Generating Java Code
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

### 2. Full Configuration to Use
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
            <!-- The following are the additional parameters for the Customer plug-in -->
            <templateLocation>D:\code-generator-maven-plugin\src\main\resources\templates</templateLocation>
            <templateType>beetl</templateType>
        </parameters>
    </configuration>
</plugin>
~~~~

## Parameter configuration instructions
|   Parameter Name | Mandatory | Sample                                                      | Description                                                                                                                                                                                                                                                                                                                                                    |
|-----------------:|:----------|:------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|    outputPackage | No        | com.github.mengweijin                                       | The package path to code generation. The default: com.github.mengweijin                                                                                                                                                                                                                                                                                        |
|           author | No        | mengweijin                                                  | Class annotation with the @Author value above. Default: take the user name of the current computer                                                                                                                                                                                                                                                             |
|  dbInfo.username | No        | root                                                        | The database connection information. If it is a standard SpringBoot project, can be omitted, automatically read application.yml/yaml/properties file.                                                                                                                                                                                                          |
|  dbInfo.password | No        | root                                                        | Same as above                                                                                                                                                                                                                                                                                                                                                  |
|       dbInfo.url | No        | jdbc:mysql://192.168.83.128:3306/test                       | Same as above                                                                                                                                                                                                                                                                                                                                                  |
|           tables | No        | sys_user, rlt_user_role                                     | Same as above                                                                                                                                                                                                                                                                                                                                                  |
|      tablePrefix | No        | sys_, rlt_                                                  | The prefix of the database table name corresponding to the code to be generated. Once configured, the generated Entity class will no longer have a table prefix. For example, User, UserRole. If not configured, the generated Entity class is prefixed with a table. For example, SysUser, RltUserRole. Multiple table name prefixes are separated by commas. |
| superEntityClass | No        | com.github.mengweijin.quickboot.mybatis.BaseEntity          | The generated Entity class inherits the parent class.                                                                                                                                                                                                                                                                                                          |
|      lombokModel | No        | true                                                        | Whether the generated Entity is Lombok enabled. Unconfigured or true: Enable Lombok mode; Set to false: If Lombok is not enabled, the generated entity contains getter/setter/toString methods.                                                                                                                                                                |
| templateLocation | Yes       | D:\code-generator-maven-plugin\src\main\resources\templates | Customer plug-in parameter only, user-defined template location. If the user wants to write the template file that generates the code himself, he can use the Customer plug-in and configure the absolute path to the folder where the template file resides. For example, it can be placed in the resources/generator/ directory of your project code.        |
|     templateType | No        | beetl                                                       | Customer plug-in parameter only, user-defined template type. Optional values are "beetl, velocity, freemarker" and the corresponding template suffix is "btl, vm, ftl". This parameter is optional. If this parameter is not configured, the default value is beetl, which means that the template suffix should end with .btl.                                |

## FAQs
1. The database table exists, but no code file is generated, and the program does not report an error.
  * Configure database table names to be exactly the same as table names in the database.
  For example, when an H2 database creates a table with a script, the script name is written in lowercase,
  but the generated table name may be in upper case, so you need to configure the upper case table name here.
2. The problem "Table \[table_name] does not exist in database!!" when generating code for a multi-module project using the H2 database. 
  * When the project structure is multi-module, the project structure is as follows:
   ````txt
   - project-parent
      - h2
         - test.mv.db
      - project-child
         - src
            - main
               - java
               - resources
         - pom.xml(The code-generator-maven-plugin is configured here)
      - pom.xml(The project-parent's pom.xml)
   ````
  * As you can see, the /h2/test.mv.db file is in the root path of the entire project
  * The URL configured in our program is jdbc:h2:file:./h2/test;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL
  * When using the code-generator-maven-plugin, the above URL cannot be configured because the root path of the plug-in in a multi-module project is project-child and is not where the /h2/test.mv.db file is located
  * At this point we can use the following two ways to manually specify:
    * Use absolute path: jdbc:h2:file:C:/Source/code/gitee/quickboot/h2/test;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL
    * Use relative paths (one more layer ../ symbol): jdbc:h2:file:./../h2/test;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL
  * Note: Only multi-module projects need to be specified this way; individual projects are not affected.
3. How do I customize templates?
    * See the template file in the src\main\resources\templates directory in the code-generator-maven-plugin project. You can also change the suffix to anything else. See the templateType parameter description.
    * Note the naming of the template file. The first paragraph is part of the name of the generated file. The second paragraph is the suffix of the generated file; The third part is the suffix of template type;
    * Template content parameter. Refer to the existing template file content, you can use in the end there are basic inside, can be used directly.

## Futures
You are welcome to suggest better ways to improve this widget.
## Contributions
You are welcome to contribute code, let more people more time to accompany the people you care about.
