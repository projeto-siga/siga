@echo on
cd ..\siga
call mvn eclipse:eclipse -Dwtpversion=2.0
cd ..\sigaex
call mvn eclipse:eclipse -Dwtpversion=2.0
cd ..\sigawf
call mvn eclipse:eclipse -Dwtpversion=2.0
cd ..\sigacd
call mvn eclipse:eclipse -Dwtpversion=2.0
cd ..\ide