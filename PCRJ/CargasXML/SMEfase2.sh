#!/bin/bash

BASE="/opt/java/xml_processo_rio"
JAVA_HOME="/usr/java/jdk1.7.0_71"
JAVA_OPTS="-Dsiga.properties.file=${BASE}/bin/siga.properties"
JAVA_FILE="${BASE}/bin/siga-cp-sinc.one-jar.jar" 
export JAVA_HOME


# arquivoXML="/opt/java/xml_processo_rio/xmls/SME.XML_20211019_02h33m02s28403"
# arquivoXML="/opt/java/xml_processo_rio/xmls/SME.XML_20211020_03h33m01s5099"
# arquivoXML="/opt/java/xml_processo_rio/xmls/SME.XML_20211028_03h33m02s21143"
source ${BASE}/bin/parametrosSME.sh
arquivoXML=`cat ${BASE}/bin/arquivoXML.txt`
  
echo -e "\n\n\n\nFoi iniciada uma carga da SME no sistema processo.rio, ambiente de producao.\n\n`ls -l ${BASE}/xmls/SME*.XML* | nl`" | mail -s "Processo rio, ambiente de producao. Aviso de inicio da carga da SME, fase 2." ${e_correio}

certo=`egrep -ic "Total de altera.*es" ${BASE}/logs/SME_modolog.log2`
# certo=`tail -33 ${BASE}/logs/SME_modolog.log2 | tr -d "\n" | egrep -ic "Total de altera.*es"`

if [[ $certo -ne 0 ]]
then
#       time curl --noproxy '*' https://acesso.processo.rio -H "Authorization: 86d034b4-cc16-43e5-bd49-ecced262dc24" -F file=@${arquivoXML} "https://acesso.processo.rio/siga/public/app/admin/sinc?sigla=SME&maxSinc=100000&modoLog=false" 1> ${BASE}/logs/SME.log2         2>&1
        time curl --noproxy '*' ${sitio1} -H "Authorization: ${chave}" -F file=@${arquivoXML} "${sitio2}?sigla=SME&maxSinc=100000&modoLog=false" 1> ${BASE}/logs/SME.log2         2>&1

        echo -e "Arquivo da SME processado com sucesso!!!\n"
        rm -f /tmp/*.data
        rm -f /tmp/*.ehcache-diskstore.lock
else
        echo -e "O arquivo SME nao foi processado pois contem erros!!!\n" 1> ${BASE}/logs/SME.log2 2>&1
        cat ${BASE}/logs/SME.*}.log2
fi

echo -e "\n\n\n\nConcluida uma carga da SME - fase 2 - no sistema processo.rio, ambiente de producao.\n\n\n Arquivo: `cat ${BASE}/bin/arquivoXML.txt` \n\n\n`egrep -ic "Total de altera.*es" ${BASE}/logs/SME*.log2 | egrep '\:0' | sort | nl` \n\n\n\n `tail -33 ${BASE}/logs/SME*.log2\n\n\n`" | mail -s "Processo rio, ambiente de producao. Aviso de conclusao de carga da SME, fase 2." ${e_correio}

unset arquivoXML
unset certo
exit 0
