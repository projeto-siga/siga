FROM daggerok/jboss-eap-7.2:7.2.5-alpine
LABEL org.opencontainers.image.authors="michelrisucci@codata.pb.gov.br"



USER root

# Bibliotecas adicionais necessárias no container
RUN sudo apk --update --no-cache add busybox-extras graphviz fontconfig ttf-freefont ttf-dejavu
RUN fc-cache -f
RUN ln -s /usr/lib/libfontconfig.so.1 /usr/lib/libfontconfig.so && \
    ln -s /lib/libuuid.so.1 /usr/lib/libuuid.so.1 && \
    ln -s /lib/libc.musl-x86_64.so.1 /usr/lib/libc.musl-x86_64.so.1
ENV LD_LIBRARY_PATH /usr/lib
RUN chmod -R 777 ${JBOSS_HOME}



USER ${JBOSS_USER}

# Diretório-base de armazenamento de arquivos da aplicação
ENV PBDOC_HOME /home/jboss/pbdoc
RUN mkdir -p ${PBDOC_HOME}
RUN chmod -R 777 ${PBDOC_HOME}

# Outros serviços que executam no JBoss
ENV DEPLOYMENTS_HOME ${JBOSS_HOME}/standalone/deployments
# https://api.github.com/repos/projeto-siga/siga-docker/releases/latest
RUN wget https://github.com/projeto-siga/siga-docker/releases/download/v1.1/ckeditor.war -O ${DEPLOYMENTS_HOME}/ckeditor.war 
# https://api.github.com/repos/assijus/blucservice/releases/latest
RUN wget https://github.com/assijus/blucservice/releases/download/v2.3.6/blucservice.war -O ${DEPLOYMENTS_HOME}/blucservice.war
# https://api.github.com/repos/projeto-siga/vizservice/releases/latest
RUN wget https://github.com/projeto-siga/vizservice/releases/download/v1.0.0.0/vizservice.war -O ${DEPLOYMENTS_HOME}/vizservice.war

# Driver JDBC do PostgreSQL
ENV JDBC_DRIVER_FILENAME 'postgresql-42.2.22.jar'
RUN mkdir -p ${JBOSS_HOME}/modules/org/postgresql/main
RUN wget https://jdbc.postgresql.org/download/${JDBC_DRIVER_FILENAME} -O ${JBOSS_HOME}/modules/org/postgresql/main/${JDBC_DRIVER_FILENAME}
RUN touch ${JBOSS_HOME}/modules/org/postgresql/main/module.xml
RUN echo "<?xml version=\"1.0\" ?><module xmlns=\"urn:jboss:module:1.1\" name=\"org.postgresql\"><resources><resource-root path=\"${JDBC_DRIVER_FILENAME}\"/></resources><dependencies><module name=\"javax.api\"/><module name=\"javax.transaction.api\"/></dependencies></module>" \
  > ${JBOSS_HOME}/modules/org/postgresql/main/module.xml



COPY /target/*.war $DEPLOYMENTS_HOME/
RUN mv ${JBOSS_HOME}/standalone/configuration/standalone.xml ${PBDOC_HOME}/standalone.xml.original
COPY /standalone.xml ${PBDOC_HOME}

# OPENSHIFT (config maps do standalone.xml com volume montado em $PBDOC_SCRIPTS_HOME)
RUN ln -s ${PBDOC_HOME}/standalone.xml ${JBOSS_HOME}/standalone/configuration/standalone.xml
