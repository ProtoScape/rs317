@echo off
title [%date%, %time%] Compile
if exist "bin/Client.class" (
javac -d bin src/*.java
echo [%date%, %time%]: Task complete.
) else (
mkdir bin
javac -d bin src/*.java
echo [%date%, %time%]: Task complete.
)
pause