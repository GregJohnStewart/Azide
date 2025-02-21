@echo off

REM KEYCLOAK
start "Key Cloak" cmd /k "cd compose && startkeycloak.bat"

REM Axide
start "Azide" cmd /k "mvnw compile quarkus:dev"

REM Landing Page
start "Landing Page" cmd /k "cd ..\MessageService && mvnw compile quarkus:dev"

REM Test Data Viewer
start "Test Data Viewer" cmd /k "cd ..\testdataviewer && mvnw compile quarkus:dev"

REM Test Data Generator
start "Test Data Generator" cmd /k "cd ..\testdatagenerator && mvnw compile quarkus:dev"

