#!/usr/bin/env bash
set -x

echo "Executing extensions.cli"
"$JBOSS_HOME/bin/jboss-cli.sh" --file="$JBOSS_HOME/extensions/extensions.cli"

if [[ -r $JBOSS_HOME/extensions/datasource.cli ]]; then
	echo "Executing datasource.cli"
	"$JBOSS_HOME/bin/jboss-cli.sh" --file="$JBOSS_HOME/extensions/datasource.cli"
fi