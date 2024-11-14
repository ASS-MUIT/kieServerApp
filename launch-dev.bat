@echo off

set mavenInput="%*"

if "%*" == "" (
	echo No Maven arguments skipping maven build
) else (
	echo Running with user input: %mavenInput%
	echo Running maven build on available project

	call mvn -v >con

	cd ..

	for %%s in ("kieserverapp") do (

			cd *%%s
			echo ===============================================================================
            for %%I in (.) do echo %%~nxI
            echo ===============================================================================

			if exist "%M3_HOME%\bin\mvn.bat" (
				call %M3_HOME%\bin\mvn.bat %* >con
			) else (
				call mvn %* >con
			)

			cd ..

	)
)

goto :startapp

:startapp
	echo "Launching the application in development mode - requires connection to controller (workbench)"
    cd kieserverapp
    cd target
    for /f "delims=" %%x in ('dir /od /b *.jar') do set latestjar=%%x
    cd ..
rem call java -Dspring.profiles.active=dev -jar target\%latestjar%
    call java -Dspring.profiles.active=dev --illegal-access=permit --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.util.concurrent=ALL-UNNAMED -jar target\%latestjar%


:end
