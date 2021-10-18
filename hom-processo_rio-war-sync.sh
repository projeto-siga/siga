#!/bin/bash
#RESUME
#This scripts is part of your deployment process. First step is previously executed a rsync or scp command to copy all war's from developer #environment.  Second step is to check environment requirements and finally execute the deploy operation.
#

#verify parameters 
if [ $# -lt 5 ]; then
   echo "ARGS NOT FOUND:  FAIL"
   echo "ARGS: JBOSS_USER JBOSS_PASS SCP_USER SERVERS_ARRAY ORIGIN_SERVER_ARTIFACTS"
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
##e.g jdevasxxx jdevaszzz jdevasyyy
servers=($4)

## DECLARE ORIGIN SERVER ARTIFACTS VARIABLE
##e.g jdevasxxx
origin_server_artifacts=($5)

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
if j_version=`javac -version 2>&1 |cut -d "\"" -f2|head -n 1`; then
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
if jboss_version=`$JBOSS_HOME/bin/standalone.sh -version`; then
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
echo "COPYING DEPENDENCIES FROM $origin_server_artifacts"

for t in ${dependencies[@]}; do
        if copy_war_jar=`scp $scp_user@$origin_server_artifacts:/tmp/$t /opt/java/jboss-eap-7.2/standalone/deployments/`; then
        
        
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
echo ""
echo "COPYING TARGETS FROM $origin_server_artifacts"

for t in ${targets[@]}; do
        if copy_war_jar=`scp $scp_user@$origin_server_artifacts:/tmp/$t /tmp`; then
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

echo "###############################################################################"
echo "                              STARTING DEPLOY"
echo "###############################################################################"
echo ""
for s in ${servers[@]}; do

echo "$s"
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
	echo ""
	done
echo "DEPLOY TARGETS:"
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
        echo ""
        done


done
echo "###############################################################################"
echo "                                        END"
echo "###############################################################################"
