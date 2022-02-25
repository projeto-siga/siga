#!/bin/bash
#RESUME
#This scripts is part of your deployment process. First step is to check environment requirements.  Second step is previously executed a scp command to copy all war's from developer or stage #environment and finally execute the deploy operation.
#

#verify parameters 
if [ $# -lt 4 ]; then
   echo "ARGS NOT FOUND:  FAIL"
   echo "ARGS: JBOSS_USER JBOSS_PASS SERVERS_ARRAY ORIGIN_SERVER_ARTIFACTS"
   echo "E.G: ./processo_rio-war-sync.sh jboss 12343 root ´jhomas456.infra.rio.gov.br jhomas987.infra.rio.gov.br´ jdevas323"
   echo "ABORTING..."
   exit 1
fi

#DECLARE CREDENTIALS
jboss_user=$1
jboss_pass=$2

## DECLARE DEPENDENCIES ARRAY
dependencies=(blucservice.war ckeditor.war vizservice.war)

## DECLARE TARGETS ARRAY
targets=(siga-ext.jar sigaex.war siga.war)

## DECLARE SERVERS ARRAY
##e.g jdevasxxx.infra.rio.gov.br jdevaszzz.infra.rio.gov.br jdevasyyy.infra.rio.gov.br
servers=($3)

## DECLARE ORIGIN SERVER ARTIFACTS VARIABLE
##e.g jdevasxxx
origin_server_artifacts=($4)

#set variables
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.272.b10-1.el7_9.x86_64/
export JBOSS_HOME=/opt/java/jboss-eap-7.2


#Verifying requirements
echo ""
echo ""
echo "################################################################################"
echo "                            VERIFYING REQUIREMENTS"
echo "################################################################################"
echo ""
echo "JAVAC VERSION:"
if j_version=`java -version 2>&1 |cut -d "\"" -f2|head -n 1`; then
        echo "$j_version"
        echo "OK"

else
        echo $j_version
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi
echo ""
echo "JBOSS VERSION:"
if jboss_version=`pgrep -f org.jboss.as`; then
        echo $jboss_version
        echo "OK"
else
        echo $jboss_version
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi
echo ""
echo "GRAPHVIZ VERSION:"
if graphviz=`rpm -qa graphviz`; then
        echo "$graphviz - OK"
else
        echo $graphviz
        echo "FAIL"
        echo "PLEASE INSTALL graphviz"
        echo "eg. sudo apk add --update --no-cache graphviz ttf-freefont or sudo yum -y install graphviz"
        echo "ABORTING..."
        exit 1
fi
echo ""
echo "###############################################################################"
echo "                                   END"
echo "###############################################################################"
echo ""

echo "###############################################################################"
echo "                              STARTING SCP"
echo "###############################################################################"
echo ""
for s in ${servers[@]}; do
        echo ""
        echo "COPYING DEPENDENCIES FROM $origin_server_artifacts TO $s"
        echo ""
                for t in ${dependencies[@]}; do
                        if copy_war_jar=`scp $origin_server_artifacts:/tmp/$t $s:/opt/java/jboss-eap-7.2/standalone/deployments/`; then
                                echo $copy_war_jar
                                echo "$t - OK"
                        else
                                echo $copy_war_jar
                                echo "FAIL"
                                echo "ABORTING..."
                                exit 1
                        fi      
                done
        echo ""
done

echo ""
echo ""
for s in ${servers[@]}; do
        echo ""
        echo "COPYING TARGETS FROM $origin_server_artifacts TO $s"

                for t in ${targets[@]}; do
                        if copy_war_jar=`scp $origin_server_artifacts:/tmp/$t $s:/tmp`; then
                                echo $copy_war_jar
                                echo "$t - OK"
                        else
                                echo $copy_war_jar
                                echo "FAIL"
                                echo "ABORTING..."
                                exit 1
                        fi
                done


        echo ""
done

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
