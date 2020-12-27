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

## 介绍
code-generator-maven-plugin 是一个在MVC项目中基于数据库表生成Controller, Service, entity, Dao(mybatis: Mapper; JPA: Repository)层CRUD代码的maven插件。
基于mybatis-plus-generator实现。

支持的框架：Mybatis, Mybatis-Plus, JPA

理论支持以下数据库：

## 如何使用?
### 1. 入门使用方式
在标准 SpringBoot 项目，以开发工具 Intellij IDEA 为例：在 Maven 中引入 code-generator-maven-plugin 插件
~~~~xml
<plugin>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>code-generator-maven-plugin</artifactId>
    <version>Latest Version</version>
</plugin>
~~~~

### 2. 一般使用方式
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