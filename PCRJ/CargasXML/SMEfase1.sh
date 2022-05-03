#!/bin/bash

BASE="/opt/java/xml_processo_rio"
JAVA_HOME="/usr/java/jdk1.7.0_71"
JAVA_OPTS="-Dsiga.properties.file=${BASE}/bin/siga.properties"
JAVA_FILE="${BASE}/bin/siga-cp-sinc.one-jar.jar" 
export JAVA_HOME

# arquivoXML="/opt/java/xml_processo_rio/xmls/SME.XML_20211019_02h33m02s28403"
# arquivoXML="/opt/java/xml_processo_rio/xmls/SME.XML_20211020_03h33m01s5099"
# arquivoXML="/opt/java/xml_processo_rio/xmls/SME.XML_20211028_03h33m02s21143"
# arquivoXML="/opt/java/xml_processo_rio/xmls/SME.XML_20211104_03h33m01s1971"
arquivoXML=`ls -1 ${BASE}/xmls/SME.XML* | tail -1`
#  | tr -d "\n"`
source ${BASE}/bin/parametrosSME.sh

echo ${arquivoXML} > ${BASE}/bin/arquivoXML.txt
# cat arquivoXML.txt | tr -d "\n" > arquivoXML.txt

rm -f ${BASE}/logs/*.log2

echo -e "\n\n\n\nFoi iniciada uma pre-carga da SME no sistema processo.rio, ambiente de producao.\n\n`ls -l ${BASE}/xmls/SME*.XML* | nl`" | mail -s "Processo rio, ambiente de producao. Aviso de inicio da pre-carga da SME." ${e_correio}

# time curl --noproxy '*' https://acesso.processo.rio -H "Authorization: 86d034b4-cc16-43e5-bd49-ecced262dc24" -F file=@${arquivoXML} "https://acesso.processo.rio/siga/public/app/admin/sinc?sigla=SME&maxSinc=100000&modoLog=true"  1> ${BASE}/logs/SME_modolog.log2 2>&1
  time curl --noproxy '*' ${sitio1} -H "Authorization: ${chave}" -F file=@${arquivoXML} "${sitio2}?sigla=SME&maxSinc=100000&modoLog=true" 1> ${BASE}/logs/SME_modolog.log2 2>&1

echo -e "\n\n\n\nConcluida uma pre-carga da SME no sistema processo.rio, ambiente de producao. \n\n\n Arquivo: `cat ${BASE}/bin/arquivoXML.txt` \n\n\n`egrep -ic "Total de altera.*es" ${BASE}/logs/SME*.log2 | egrep '\:0' | sort | nl`" | mail -s "Processo rio, ambiente de producao. Aviso de conclusao de pre-carga da SME." ${e_correio}

unset arquivoXML
exit 0
