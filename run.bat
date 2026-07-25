@echo off
echo ========================================================
echo Starting PreethiMart Capstone Platform
echo Java Servlets + JDBC + Tomcat 9 + Embedded H2
echo ========================================================
call "C:\Users\hp\apache-maven-3.9.6\bin\mvn.cmd" compile exec:java
pause
