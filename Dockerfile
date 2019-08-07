# -------------------------- Dockerfile (jboss) -------------------------- #
FROM registry.infraero.gov.br/siga-base:latest
MAINTAINER diogorocha@infraero.gov.br

#--- APLICACÕES WEB (siga) ---
COPY target/siga.war target/sigaidp.war target/sigaex.war /opt/jboss/sigadoc/deployments/
COPY target/siga-ext.jar /opt/jboss-eap-6.2/modules/sigadoc/ext/main/siga-ext.jar
