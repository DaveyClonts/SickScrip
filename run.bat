@echo off

REM Compile Java files into bin/
javac -d bin src\scrip\*.java

IF %ERRORLEVEL% NEQ 0 (
    echo Compilation failed.
    pause
    exit /b
)

echo Compilation successful.
echo Type "exit" to quit.
echo.

:loop
SET SCRIP_FILE=
SET /P SCRIP_FILE=Enter .sic script file to run:

IF /I "%SCRIP_FILE%"=="exit" goto done
IF "%SCRIP_FILE%"=="" goto loop

java -cp bin scrip.Sic "%SCRIP_FILE%"

echo.
goto loop

:done
echo Bye!