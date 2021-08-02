FROM timon.cloud.prodesp.sp.gov.br/9990197/eap74-openjdk8-openshift-rhel7

ADD ./eap/modules.tar.gz ${JBOSS_HOME}/

COPY target/siga.war target/sigaex.war target/sigawf.war /deployments/
COPY eap/postconfigure.sh eap/extensions.cli /opt/eap/extensions/

RUN curl -sk https://api.github.com/repos/projeto-siga/siga-docker/releases/latest | grep browser_download_url | grep .war | cut -d '"' -f 4 | xargs curl -L -o /deployments/ckeditor.war \
    && chown jboss:jboss /deployments/ckeditor.war

USER root

RUN chown -R jboss:root ${JBOSS_HOME}/modules /opt/eap/extensions /deployments/*.war

USER jboss
