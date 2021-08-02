#!/usr/bin/env bash
set -x

if [ "${ENABLE_SIGA_WF^^}" != "TRUE" ]; then
	echo Disabling sigawf.rar deployment
	touch /deployments/sigawf.war.skipdeploy
fi

echo "Executing extensions.cli"
$JBOSS_HOME/bin/jboss-cli.sh --file=$JBOSS_HOME/extensions/extensions.cli -DENABLE_SIGA_WF="${ENABLE_SIGA_WF^^}"
