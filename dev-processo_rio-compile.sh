#!/bin/bash

#verify args
if [ $# -eq 0 ]; then
        echo "Not found Maven Parameters: eg.:'-T 1C -pl !siga-dump,!siga-ex-gsa,!siga-ex-sinc,!siga-gc-gsa,!sigagc,!sigasr,!sigapp,!siga-ldap-cli,!sigatp,!siga-wf,!sigawf,!siga-vraptor-module-old install -DskipTests=true -DskipTests=true' jdevas135.infra.rio.gov.br:9990 ${WORKSPACE}"
        exit 1
fi

#set variables

export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.272.b10-1.el7_9.x86_64/
JBOSS_HOME=/opt/java/jboss-eap-7.2


#Verify requirements
echo ""
echo ""
echo ""
echo "################################################################################"
echo "                            REQUIREMENTS VERIFICATION"
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
echo "JBOSS PID:"
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
echo "MAVEN VERSION:"
if m_version=`mvn -version`; then
        echo "$m_version"
        echo "OK"
else
        echo m_version
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
        echo "eg. sudo apk add --update --no-cache graphviz ttf-freefont  or   sudo yum -y install graphviz"
        echo "ABORTING..."
        exit 1
fi
echo ""

echo "VIZSERVICE:"
curl -s https://api.github.com/repos/projeto-siga/vizservice/releases/latest | grep browser_download_url | grep vizservice.war | cut -d '"' -f 4 | xargs wget -q downloading vizservice.war
if verify_vizservice_war=`ls -l vizservice.war`; then
        echo "DOWNLOAD: vizservice.war - OK"
        cp -rf vizservice.war ~/
                if deploy_vizservice=`mv -f vizservice.war $JBOSS_HOME/standalone/deployments/`; then
                        echo "DEPLOY: vizservice.war - OK"
                else
                        echo $deploy_vizservice
                        echo "FAIL"
                        echo "ABORTING..."
                        exit 1
                fi
else
        echo "FAIL"
        echo "DO DOWNLOAD vizservice.war FROM https://api.github.com/repos/projeto-siga/vizservice/releases/latest AND MOVE TO $JBOSS_HOME/standalone/deployments/"
        echo "ABORTING..."
        exit 1
fi
echo ""

echo "BLUCSERVICE:"
curl -s https://api.github.com/repos/assijus/blucservice/releases/latest | grep browser_download_url | grep blucservice.war | cut -d '"' -f 4 | xargs wget -q downloading blucservice.war

if verify_blucservice_war=`ls -l blucservice.war`; then
        echo "DOWNLOAD: blucservice.war - OK"
        cp -rf blucservice.war ~/
                if deploy_blucservice=`mv -f blucservice.war $JBOSS_HOME/standalone/deployments/`; then
                        echo "DEPLOY: blucservice.war - OK"
                else
                        echo $deploy_blucservice
                        echo "FAIL"
                        echo "ABORTING..."
                        exit 1
                fi
else
        echo "FAIL"
        echo "DO DOWNLOAD blucservice.war FROM https://api.github.com/repos/assijus/blucservice/releases/latest AND MOVE TO $JBOSS_HOME/standalone/deployments/"
        echo "ABORTING..."
        exit 1
fi
echo ""

echo "CKEDITOR:"
curl -s https://api.github.com/repos/projeto-siga/siga-docker/releases/latest | grep browser_download_url | grep .war | cut -d '"' -f 4 | xargs wget -q
if verify_ckeditor_war=`ls -l ckeditor.war`; then
        echo "DOWNLOAD: ckeditor.war - OK"
        cp -rf ckeditor.war ~/
                if deploy_ckeditor=`mv -f ckeditor.war $JBOSS_HOME/standalone/deployments/`; then
                        echo "DEPLOY: ckeditor.war - OK"
                else
                        echo $deploy_ckeditor
                        echo "FAIL"
                        echo "ABORTING..."
                        exit 1
                fi
else
        echo "FAIL"
        echo "DO DOWNLOAD ckeditor.war FROM  https://api.github.com/repos/projeto-siga/siga-docker/releases/latest AND MOVE TO $JBOSS_HOME/standalone/deployments/"
        echo "ABORTING..."
        exit 1
fi
echo ""
echo "###############################################################################"
echo "                                   END"
echo "###############################################################################"
echo ""
echo ""
echo ""
echo "###############################################################################"
echo "                              STARTING MAVEN"
echo "###############################################################################"
echo ""
echo ""
mvn $1
echo ""
echo "###############################################################################"
echo "                                   END"
echo "###############################################################################"
echo ""

echo ""
echo "###############################################################################"
echo "                              STARTING DEPLOY"
echo "###############################################################################"
echo ""

cp -rf target/* ~/

echo "SIGA:"
#if deploy_siga=`/opt/java/jboss-eap-7.2/bin/jboss-cli.sh --connect --command="deployment deploy-file --replace $3/target/siga.war"`; then
#if deploy_siga=`/opt/java/jboss-eap-7.2/bin/jboss-cli.sh --connect --command="deployment deploy-file $JBOSS_HOME/standalone/deployments/siga.war"`; then
if deploy_siga=`cp -rf $3/target/siga.war $JBOSS_HOME/standalone/deployments/`; then
        echo "DEPLOY: siga.war - OK"
else
        echo $deploy_siga
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi
echo ""


echo "SIGAEX:"
if deploy_sigaex=`cp -rf $3/target/sigaex.war $JBOSS_HOME/standalone/deployments/`; then
        echo "DEPLOY: sigaex.war - OK"
else
        echo $deploy_sigaex
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi
echo ""


echo "SIGA-EXT:"
echo "     Undeploying module SIGA-EXT:"
if [ -f /opt/java/jboss-eap-7.2/modules/sigadoc/ext/main/siga-ext.jar ]; then
#if module_siga_ext=`ls /opt/java/jboss-eap-7.2/modules/sigadoc/ext/main/siga-ext.jar`; then
        if module_siga_ext_remove=`/opt/java/jboss-eap-7.2/bin/jboss-cli.sh --connect --command="module remove --name=sigadoc.ext"`; then
              echo "REMOVE OLD MODULE: OK"
        else
                echo $module_siga_ext_remove
                echo "FAIL"
                echo "ABORTING..."
                exit 1
        fi
fi

echo "     Deploying module SIGA-EXT:"
if module_siga_ext=`/opt/java/jboss-eap-7.2/bin/jboss-cli.sh --connect controller=remote+http://${2} --command="module add --name=sigadoc.ext --resources=~/siga-ext.jar" --timeout=30000`; then
        echo "DEPLOY MODULE: siga-ext.jar - OK"
else
        echo $module_siga_ext
        echo "FAIL"
        echo "ABORTING..."
        exit 1
fi        

echo ""
echo "###############################################################################"
echo "                              END"
echo "###############################################################################"
