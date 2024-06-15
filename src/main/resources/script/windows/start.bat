rem 使用者应根据自身平台编码自行转换 防止乱码 例如 win使用gbk编码
@echo off
title=App-Service

echo  启动倒计时：
timeout /T 10 /NOBREAK

rem jar平级目录
set AppName=app-admin.jar

rem JVM参数
set JVM_OPTS="-Dname=%AppName% -Duser.timezone=Asia/Shanghai -Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC"

for /f "usebackq tokens=1-2" %%a in (`jps -l ^| findstr %AppName%`) do (
	set pid=%%a
	set image_name=%%b
)
if  defined pid (
	echo %%is running
	PAUSE
)

echo  starting……
java %JVM_OPTS% -jar %AppName%
PAUSE

