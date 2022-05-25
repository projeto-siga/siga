#!/bin/bash

#
# *******************************************************************************
# * PCRJ     - Prefeitura da cidade do Rio de Janeiro                           *
# * SEGOVI   - Secretaria de Governo (cliente)                                  *
# * IPLANRIO - Empresa Municipal de Informática                                 *
# *                                                                             *
# * Supervisor: Ivan Gonçalves Silvério                                         *
# *                                                                             *
# * Autor: Jorge Luís de Oliveira Borges                                        *
# * Nota: a primeira versão deste programa foi feita pela DOP em 2020.          *
# *                                                                             *
# * E-mails: (ivan.silverio, jborges)@rio.rj.gov.br                             *
# *                                                                             *
# * Data: 08 de dezembro de 2021                                                *
# *                                                                             *
# * Objetivo: Efetua a carga do usuários do processo.rio.                       *
# *                                                                             *
# * © Copyright 2000-2022 Jorge Luís de Oliveira Borges.                        *
# *                                                                             *
# *   Respeite os direitos autorais.                                            *
# *                                                                             *
# *                                         Ana Beatriz Barbosa Silva:          *
# *                                              "A vida é bumerangue:          *
# *                                               tudo que vem tem volta." :)   *
# *                                                                             *
# *******************************************************************************
#

# declaração de variáveis
BASE="/opt/java/xml_processo_rio"
JAVA_HOME="/usr/java/jdk1.7.0_71"
JAVA_OPTS="-Dsiga.properties.file=${BASE}/bin/siga.properties"
JAVA_FILE="${BASE}/bin/siga-cp-sinc.one-jar.jar" 
export JAVA_HOME

certo=0
certo2=0
certo3=0
tempo1=`date`
ThisXML=""
ThisSigla=""
ThisLogPreCarga=""
ThisLogcarga=""
ThisExcluidos=""
ThisPessoas=""
ThisCargaUnica=""


# Inclui os parâmetros deste ambiente.
source ${BASE}/bin/parametros.sh

# testa se o tipo de carga é completa, ou de uma única secretaria.
if [[ -z ${1} ]]
then
        # RHUPAG
#       mv ${BASE}/xmls/IC.XML ${BASE}/xmls/IC.XXMMLL_`date +%Y%m%d_%Hh%Mm%Ss$RANDOM`
#       mv ${BASE}/xmls/TUR.XML ${BASE}/xmls/TUR.XXMMLL_`date +%Y%m%d_%Hh%Mm%Ss$RANDOM`
#       mv ${BASE}/xmls/ZOO.XML ${BASE}/xmls/ZOO.XXMMLL_`date +%Y%m%d_%Hh%Mm%Ss$RANDOM`
#       mv ${BASE}/xmls/CEN.XML ${BASE}/xmls/CEN.XXMMLL_`date +%Y%m%d_%Hh%Mm%Ss$RANDOM`
  
        mv ${BASE}/xmls/SME.XML ${BASE}/xmls/SME.XML_`date +%Y%m%d_%Hh%Mm%Ss$RANDOM`
        mv ${BASE}/xmls/XXX.XML ${BASE}/xmls/XXX.XML_`date +%Y%m%d_%Hh%Mm%Ss$RANDOM`
        mv ${BASE}/xmls/ServidoresSemUABasica.XML ${BASE}/xmls/ServidoresSemUABasica.XML_`date +%Y%m%d_%Hh%Mm%Ss$RANDOM`
        mv ${BASE}/xmls/ServidoresDemUABasica.XML ${BASE}/xmls/ServidoresDemUABasica.XML_`date +%Y%m%d_%Hh%Mm%Ss$RANDOM`
        
        SEARCH="${BASE}/xmls/*.xml ${BASE}/xmls/*.XML"
        rm -f ${BASE}/logs/*.log
#		Passa um email de início da carga quando a carga é completa.
        echo -e "\n\n\n\nFoi iniciada uma carga completa no sistema processo.rio, ${ambiente}.\n\n inicio: $tempo1 \n\n`ls -l ${BASE}/xmls/*.XML | nl`" | mail -s "Processo rio, ${ambiente}. Aviso de inicio da carga completa." ${e_correio} &

else
        SEARCH="${BASE}/xmls/${1}"
		ThisCargaUnica="${1}"
fi

# Efetua a carga para a(s) secretarias selecionadas.
for F in $( ls -1r ${SEARCH} )
do

    FN=${F##*/}
	ThisXML="${BASE}/xmls/${FN%%.*}.XML"
	ThisLogPreCarga="${BASE}/logs/${FN%%.*}_modolog.log"
	ThisLogCarga="${BASE}/logs/${FN%%.*}.log"
	ThisSigla="${FN%%.*}"
	
#   Testa se a carga do arquivo XML será bem sucedida, porem não efetua as atualizações.
    curl --noproxy '*' ${sitio1} -H "Authorization: ${chave}" -F file=@${ThisXML} "${sitio2}?sigla=${ThisSigla}&maxSinc=100000&modoLog=true" 1> ${ThisLogPreCarga} 2>&1

    ThisExcluidos=`egrep -ic excluindo ${ThisLogPreCarga}`
	ThisPessoas=`egrep -ic 'pessoa ' ${ThisXML}`
#   Verifica a carga foi bem sucedida, ou não.
#   Verifica se o layout é de uma carga mesmo ou se é apenas o relatório de erro.
    certo=`egrep -ic "Total de altera.*es" $ThisLogPreCarga`
    layout=`egrep -ic "XML confirma .*rg.*o usu.*rio" $ThisLogPreCarga`
# 1 liga o certo, 0 desliga o certo (errado)
#   certo=1  # Efetua a carga na força bruta
    echo -e "${FN%%.*} modo log: $certo"
# 1 informa que é o log do aplicativo de carga (API REST), 0 trata-se do relatório do 'parsing'
    echo -e "Layout $layout"
    echo -e "ThisExcluidos $ThisExcluidos, ThisPessoas $ThisPessoas"
    
#   continue

#   se o certo está ligado, efetua a carga sabendo o gabarito previamente.
#   o layout define que trata-se do layout da carga do aplicativo (1) contrapondo-se ao layout de erro de parsing (0)
    if [[ $certo -ne 0 && $layout -ne 0 ]]; then
#      if [[ $ThisExcluidos -ge $ThisPessoas ]]; then
#      if [[ $ThisPessoas   -lt $ThisExcluidos ]]; then
       if [[ $ThisPessoas   -gt $ThisExcluidos || $ThisSigla = "CVL777" ]]; then 
   #      efetua a carga propriamente dita	
          curl --noproxy '*' ${sitio1} -H "Authorization: ${chave}" -F file=@${ThisXML} "${sitio2}?sigla=${ThisSigla}&maxSinc=100000&modoLog=false" 1> ${ThisLogCarga} 2>&1

          echo -e "Arquivo ${F##*/} processado com sucesso!!!\n"
#         apaga arquivos temporários para não ter problemas de espaço em disco.	   
          rm -f /tmp/*.data
          rm -f /tmp/*.ehcache-diskstore.lock
       else # Efetua a carga normalmente
          # avisa que detectou uma incoerência no arquivo XML
          echo -e "\n\n\n\nO arquivo ${ThisXML} veio com ${ThisPessoas} pessoas, e na pre-carga constam ${ThisExcluidos} exclusoes, ${ambiente}. \n\n\n O arquivo em tela necessita de conferencia manual.\n" | mail -s "Processo rio, ${ambiente}, alerta de carga com muitas exclusoes. ${ThisSigla}" ${e_correio}

          echo -e "O arquivo ${F##*/} nao foi processado pois contem erros!!!\n" 1> ${ThisLogCarga} 2>&1
          cat ${ThisLogCarga}

       fi
         
    else
            echo -e "O arquivo ${F##*/} nao foi processado pois contem erros!!!\n" 1> ${ThisLogCarga} 2>&1
            cat ${ThisLogCarga}
    fi
done

# Passa um email informado dados do fim da carga
echo -e "\n\n\n\nConcluida uma carga no sistema processo.rio, ${ambiente}. \n\nInicio : $tempo1, \nTermino: `date` \n\n\n Os arquivos abaixo necessitam de conferencia manual:\n`egrep -ic "Total de altera.*es" ${BASE}/logs/*.log | egrep '\:0' | sort | nl`" | mail -s "Processo rio, ${ambiente}. Aviso de conclusao de carga. ${ThisCargaUnica}" ${e_correio}

# Apaga os arquivos compactados mais velhos do que o valor informado em dias.
find ${BASE}/zips -maxdepth 1 -mtime +90 -exec rm {} \; &

# Apaga os arquivos da SME mais velhos do que o valor informado em dias.
find ${BASE}/xmls -maxdepth 1 -mtime +90 -name SME.XML_20*  -exec rm {} \; &
find ${BASE}/xmls -maxdepth 1 -mtime +90 -name ServidoresDemUABasica.XML_20* -exec rm {} \; &
find ${BASE}/xmls -maxdepth 1 -mtime +90 -name CEN.XXMMLL_20*  -exec rm {} \; &

if [[ -z ${1} ]]
then
#   No caso de haver uma carga completa, salvaguarda os arquivos de carga e os seus logs
    tar -czf ${BASE}/zips/XML_`date +%Y%m%d_%Hh%Mm%Ss_$RANDOM`.tgz ${BASE}/xmls/*.XML &
#   tar -czf ${BASE}/zips/XXMMLL_`date +%Y%m%d_%Hh%Mm%Ss_$RANDOM`.tgz ${BASE}/xmls/*.XXMMLL_20* &
    tar -czf ${BASE}/zips/LOG_`date +%Y%m%d_%Hh%Mm%Ss_$RANDOM`.tgz ${BASE}/logs/*.log &

fi

# 'destroi' as variáveis
unset certo
unset layout

unset chave
unset sitio1
unset sitio2
unset e_correio

exit 0

#
# ******************************************************************************* 
# *                                    FIM                                      *
# *******************************************************************************
#
