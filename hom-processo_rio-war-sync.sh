#!/bin/bash
#RESUME
#This scripts is part of your deployment process. First step is to check environment requirements.  Second step is previously executed a scp command to copy all war's from developer or stage #environment and finally execute the deploy operation.
#

#verify parameters 
if [ $# -lt 5 ]; then
   echo "ARGS NOT FOUND:  FAIL"
   echo "ARGS: JBOSS_USER JBOSS_PASS SCP_USER SERVERS_ARRAY ORIGIN_SERVER_ARTIFACTS"
   echo "E.G: ./processo_rio-war-sync.sh jboss 12343 root ´jhomas456.infra.rio.gov.br jhomas987.infra.rio.gov.br´ jdevas323"
   echo "ABORTING..."
   exit 1
fi

#DECLARE CREDENTIALS
jboss_user=$1
jboss_pass=$2

#DECLARE SCP USER
scp_user=$3

## DECLARE DEPENDENCIES ARRAY
dependencies=(blucservice.war ckeditor.war vizservice.war)

## DECLARE TARGETS ARRAY
targets=(siga-ext.jar sigaex.war siga.war)

## DECLARE SERVERS ARRAY
##e.g jdevasxxx.infra.rio.gov.br jdevaszzz.infra.rio.gov.br jdevasyyy.infra.rio.gov.br
servers=($4)

## DECLARE ORIGIN SERVER ARTIFACTS VARIABLE
##e.g jdevasxxx
origin_server_artifacts=($5)

#set variables
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.272.b10-1.el7_9.x86_64/
export JBOSS_HOME=/opt/java/jboss-eap-7.2



echo "###############################################################################"
echo "                              SYNCHRONIZING DEPLOY"
echo "###############################################################################"
echo ""
echo "COPYING DEPENDENCIES FROM $origin_server_artifacts"
for s in ${servers[@]}; do
  echo "SYNC DEPENDENCIES WITH SERVER: $s"
        for t in ${dependencies[@]}; do
                if copy_war_jar=`scp /tmp/$t $s:/opt/java/jboss-eap-7.2/standalone/deployments/`; then                        
                        echo $copy_war_jar
                        echo "$t - OK"
                else
                        echo $copy_war_jar
                        echo "FAIL"
                        echo "ABORTING..."
                        exit 1
                fi
        done
done

echo ""
echo ""
echo "COPYING TARGETS FROM $origin_server_artifacts"
for s in ${servers[@]}; do
echo "SYNC TARGETS WITH SERVER: $s"
        for t in ${targets[@]}; do
                if copy_war_jar=`scp /tmp/$t $s:/tmp`; then
                        echo $copy_war_jar
                        echo "$t - OK"
                else
                        echo $copy_war_jar
                        echo "FAIL"
                        echo "ABORTING..."
                        exit 1
                fi
        done
done

echo ""

echo "###############################################################################"
echo "                              STARTING DEPLOY"
echo "###############################################################################"
echo ""
for s in ${servers[@]}; do
        echo "$s"
        echo ""
        echo "DEPLOY DEPENDENCIES:"
                #DEPLOY DEPENDENCIES
	        for t in ${dependencies[@]}; do	
		        if deploy_siga_dependencies=`/opt/java/jboss-eap-7.2/bin/jboss-cli.sh --connect controller=$s:9990 --command="deployment deploy-file --replace /opt/java/jboss-eap-7.2/standalone/deployments/$t" --user=$jboss_user --password=$jboss_pass`; then
			        echo "DEPLOY: $t - OK"
		        else
			        echo $deploy_siga_dependencies
			        echo "FAIL"
			        echo "ABORTING..."
			exit 1
		        fi
	
	        done
        echo ""

        echo "DEPLOY MODULE TARGETS:"
        echo "SIGA-EXT"
        if module_siga_ext=`ls /opt/java/jboss-eap-7.2/modules/sigadoc/ext/main/siga-ext.jar`; then
                if module_siga_ext_remove=`/opt/java/jboss-eap-7.2/bin/jboss-cli.sh --connect controller=$s:9990 --command="module remove --name=sigadoc.ext" --user=$jboss_user --password=$jboss_pass`; then
                        echo "REMOVE OLD MODULE: OK"
                else
                        echo $module_siga_ext_remove
                        echo "FAIL"
                        echo "ABORTING..."
                        exit 1
                fi
        fi


        if module_siga_ext=`/opt/java/jboss-eap-7.2/bin/jboss-cli.sh --connect controller=$s:9990 --command="module add --name=sigadoc.ext --resources=/tmp/siga-ext.jar" --user=$jboss_user --password=$jboss_pass`; then
                echo "DEPLOY MODULE: siga-ext.jar - OK"
        else
                echo $module_siga_ext
                echo "FAIL"
                echo "ABORTING..."
                exit 1
        fi        
echo ""

echo "DEPLOY WAR TARGETS:"
        #DEPLOY TARGETS
        for t in ${targets[@]}; do
	        if deploy_siga=`/opt/java/jboss-eap-7.2/bin/jboss-cli.sh --connect controller=$s:9990 --command="deployment deploy-file --replace /tmp/$t" --user=$jboss_user --password=$jboss_pass`; then
                        echo "DEPLOY: $t - OK"
	        else
                        echo $deploy_siga
                        echo "FAIL"
                        echo "ABORTING..."
                        exit 1
	        fi
        done
echo ""
echo ""
done
echo "###############################################################################"
echo "                                        END"
echo "###############################################################################"
