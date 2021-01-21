call mvn clean
call mvn -pl "!siga-dump,!siga-ex-gsa,!siga-ex-sinc,!siga-gc-gsa,!sigagc,!sigasr,!sigapp,!siga-ldap-cli,!sigatp,!siga-wf,!sigawf" clean install -DskipTests=true
del C:\iplanrio\jboss-eap-7.2\standalone\deployments\siga*.*
cd target
copy *.* C:\iplanrio\jboss-eap-7.2\standalone\deployments\
cd ..
cls