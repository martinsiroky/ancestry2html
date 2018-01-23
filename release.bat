echo "Change version.txt info first!!! "
pause

call mvn release:prepare
pause
call mvn release:perform

pause
