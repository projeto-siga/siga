#!/bin/bash
#RESUME
#This scripts is part of your deployment process. First step is previously executed a rsync or scp command to copy all war's from developer #environment.  Second step is to check environment requirements and finally execute the deploy operation.
#

#set variables
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.272.b10-1.el7_9.x86_64/
export JBOSS_HOME=/opt/java/jboss-eap-7.2


#Verify requirements
echo ""
echo ""
echo "################################################################################"
echo "                            REQUIREMENTS VERIFICATION"
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


## DECLARE DEPENDENCIES ARRAY
dependencies=(blucservice.war ckeditor.war vizservice.war)

## DECLARE TARGETS ARRAY
targets=(siga-ext.jar sigaex.war siga.war)






echo "###############################################################################"
echo "                              STARTING SCP"
echo "###############################################################################"
echo ""
echo "COPYING DEPENDENCIES:"

for t in ${dependencies[@]}; do
  if copy_war_jar=`scp jboss@jdevas135:/opt/java/jboss-eap-7.2/standalone/deployments/$t /opt/java/jboss-eap-7.2/standalone/deployments/`; then
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
echo "COPYING TARGETS:"

for t in ${targets[@]}; do
if copy_war_jar=`scp jboss@jdevas135:/opt/java/jenkins/workspace/processo.rio/target/$t /tmp`; then
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

for t in ${targets[@]}; do
echo "$t:"
if deploy_siga=`/opt/java/jboss-eap-7.2/bin/jboss-cli.sh --connect --command="deployment deploy-file --replace /tmp/$t"`; then
        echo "DEPLOY: $t - OK"
else
        echo $deploy_siga
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi
echo ""
done
echo ""

echo "###############################################################################"
echo "                              END"
echo "###############################################################################"

echo ""
echo "###############################################################################"
echo "                              SYNCHRONIZING WITH GROUP SERVERS"
echo "###############################################################################"

