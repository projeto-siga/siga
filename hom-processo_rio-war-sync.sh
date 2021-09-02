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
echo "###############################################################################"
echo "                              STARTING SCP"
echo "###############################################################################"
echo ""
echo "COPYING DEPENDENCIES:"
if copy_war_jar=`scp jboss@jdevas135:/opt/java/jboss-eap-7.2/standalone/deployments/blucservice.war /opt/java/jboss-eap-7.2/standalone/deployments/`; then
        echo $copy_war_jar
        echo "blucservice.war - OK"
else
        echo $copy_war_jar
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi

if copy_war_jar=`scp jboss@jdevas135:/opt/java/jboss-eap-7.2/standalone/deployments/ckeditor.war /opt/java/jboss-eap-7.2/standalone/deployments/`; then
        echo $copy_war_jar
        echo "ckeditor.war - OK"
else
        echo $copy_war_jar
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi

if copy_war_jar=`scp jboss@jdevas135:/opt/java/jboss-eap-7.2/standalone/deployments/vizservice.war /opt/java/jboss-eap-7.2/standalone/deployments/`; then
        echo $copy_war_jar
        echo "vizservice.war - OK"
else
        echo $copy_war_jar
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi
echo ""
echo ""
echo "COPYING TARGETS:"

if copy_war_jar=`scp jboss@jdevas135:/opt/java/jenkins/workspace/processo.rio/target/siga-ext.jar /tmp`; then
        echo $copy_war_jar
        echo "siga-ext.jar - OK"
else
        echo $copy_war_jar
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi

if copy_war_jar=`scp jboss@jdevas135:/opt/java/jenkins/workspace/processo.rio/target/sigaex.war /tmp`; then
        echo $copy_war_jar
        echo "sigaex.war - OK"
else
        echo $copy_war_jar
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi

if copy_war_jar=`scp jboss@jdevas135:/opt/java/jenkins/workspace/processo.rio/target/siga.war /tmp`; then
        echo $copy_war_jar
        echo "siga.war - OK"
else
        echo $copy_war_jar
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi
echo ""

echo "###############################################################################"
echo "                              STARTING DEPLOY"
echo "###############################################################################"
echo ""
echo "SIGA:"
if deploy_siga=`/opt/java/jboss-eap-7.2/bin/jboss-cli.sh --connect --command="deployment deploy-file --replace /tmp/siga.war"`; then
        echo "DEPLOY: siga.war - OK"
else
        echo $deploy_siga
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi
echo ""
echo "SIGAEX:"
if deploy_sigaex=`/opt/java/jboss-eap-7.2/bin/jboss-cli.sh --connect --command="deployment deploy-file --replace /tmp/sigaex.war"`; then
        echo "DEPLOY: sigaex.war - OK"
else
        echo $deploy_sigaex
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi
echo ""
echo "SIGA-EXT"
if deploy_siga_ext=`/opt/java/jboss-eap-7.2/bin/jboss-cli.sh --connect --command="deployment deploy-file --replace /tmp/siga-ext.jar"`; then
        echo "DEPLOY: siga-ext.war - OK"
else
        echo $deploy_siga_ext
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi
echo ""
echo "###############################################################################"
echo "                              END"
echo "###############################################################################"
