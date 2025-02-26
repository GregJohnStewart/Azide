@echo off

REM KEYCLOAK
start "Key Cloak" cmd /k "cd compose && startkeycloak.bat"

echo Waiting for 20 seconds to allow Key Cloak to startup
timeout /t 20 /nobreak

REM Axide
start "Azide" cmd /k "mvnw compile quarkus:dev"

echo Waiting for 10 seconds to allow Azide to startup
timeout /t 10 /nobreak

REM Landing Page
start "Landing Page" cmd /k "cd ..\MessageService && mvnw compile quarkus:dev"

echo Waiting for 10 seconds to allow Landing Page to startup
timeout /t 10 /nobreak

REM Test Data Viewer
start "Test Data Viewer" cmd /k "cd ..\testdataviewer && mvnw compile quarkus:dev"

echo Waiting for 10 seconds to allow Test Data Viewer to startup
timeout /t 10 /nobreak

REM Test Data Generator
start "Test Data Generator" cmd /k "cd ..\testdatagenerator && mvnw compile quarkus:dev"

