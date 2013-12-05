@echo off
rd /s /q c:\J2EE\apache-tomcat-7.0.39\webapps\demo
call mvn clean package 
cp target\demo.war c:\J2EE\apache-tomcat-7.0.39\webapps
echo.
echo Success
call C:\J2EE\apache-tomcat-7.0.39\bin\catalina.bat run
