echo Iniciando instalaçao do Play! Framework 1.3.x no repositório maven local
IF exist  %HOMEPATH%\.m2\repository\com\google\code\maven-play-plugin\org\playframework\play\1.3.x\ ( echo play repository dir exists ) ELSE ( mkdir  %HOMEPATH%\.m2\repository\com\google\code\maven-play-plugin\org\playframework\play\1.3.x\ && echo play repository dir created)
call mvn install:install-file -Dfile=play-1.3.x.jar -DgroupId=com.google.code.maven-play-plugin.org.playframework -DartifactId=play -Dversion=1.3.x -Dpackaging=jar -DpomFile=play-1.3.x.pom
copy play-1.3.x-framework-min.zip  %HOMEPATH%\.m2\repository\com\google\code\maven-play-plugin\org\playframework\play\1.3.x\

echo Play! Framework 1.3.x instalado com sucesso
