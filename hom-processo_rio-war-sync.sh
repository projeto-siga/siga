#!/bin/bash
#RESUME
#This scripts is part of your deployment process. First step is previously executed a rsync or scp command to copy all war's from developer #environment.  Second step is to check environment requirements and finally execute the deploy operation.
#

#set variables

export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.272.b10-1.el7_9.x86_64/
JBOSS_HOME=/opt/java/jboss-eap-7.2


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
scp jboss@jdevas135:/opt/java/jenkins/workspace/processo.rio/target/*.{war,jar} /tmp

if list_war_jar=`ls /tmp/*.{war,jar}`; then
        echo "$list_war_jar"
        echo "OK"

else
        echo $list_war_jar
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi
