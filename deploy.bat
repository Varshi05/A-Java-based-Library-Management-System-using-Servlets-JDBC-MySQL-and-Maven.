@echo off
REM Builds the WAR and copies it into Tomcat's webapps folder.
REM Requires: Maven (mvn) on PATH and the CATALINA_HOME environment variable set.

if "%CATALINA_HOME%"=="" (
    echo CATALINA_HOME is not set. Point it to your Tomcat folder first.
    exit /b 1
)

call mvn clean package
if errorlevel 1 (
    echo Build failed.
    exit /b 1
)

del /q "%CATALINA_HOME%\webapps\library-management-system.war" 2>nul
rmdir /s /q "%CATALINA_HOME%\webapps\library-management-system" 2>nul
copy /y "target\library-management-system.war" "%CATALINA_HOME%\webapps\"

echo.
echo Deployed. Open http://localhost:8080/library-management-system/