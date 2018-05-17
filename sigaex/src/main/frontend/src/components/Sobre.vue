<template>
  <div class="container content pt-5">
    <div class="row">
      <div class="col-md-12 sobre">
        <div class="headline pb-1">
          <h2>Por que Balcão Virtual?</h2>
        </div>
  
        <p>O Balcão Virtual do TRF2 é um site que permite o envio de petições intercorrentes em lote.</p>
  
        <div class="headline pb-1">
          <h2>Como faço para usar?</h2>
        </div>
  
        <p>Para fazer protocolar petições intercorrentes, siga o seguinte procedimento:
        </p>
        <ol>
          <li>Renomeie os arquivos PDF que contém as petições intercorrentes de modo que ele iniciem com o número do processo ao qual a petição se refere. Depois do número do processo, pode vir qualquer outra indicação do conteúdo do arquivo. Exemplos de nomes de arquivo para petições intercorrentes: 0527484-17.2007.4.02.5101.pdf, 01004835520144020000.pdf</li>
          <li>Caso deseje incluir anexos, após o número do processo, insira a informação do número do anexo, na forma "-01", "-02", etc. Exemplos de nome de arquivo anexo à petição intercorrente: 0527484-17.2007.4.02.5101-01.pdf
          </li>
          <li>Selcione a opção de menu "Petição Intercorrente"</li>
          <li>Clique na área marcada com "Arraste suas petições intercorrentes em formato PDF e solte elas nesta área, ou clique aqui para selecioná-las" e escolha os arquivos que deseja enviar.</li>
          <li>Selecione o tipo da petição e diga se há segredo de justiça para cada um dos arquivos.</li>
          <li>Se desejar enviar uma mesma petição para mais de um processo, use um nome de arquivo que não contenha nenhum número de processo. Na interface, ao lado do arquivo aparecerá um botão com símbolo "+". Clique nesse botão e forneça um lista dos números de todos os processos onde quer protocolar esta petição e clique em "Prosseguir".
            <li>Clique no botão "Protocolar" e observe a coluna "Status" para saber se as petições foram protocoladas ou se ocorreu algum erro.
            </li>
        </ol>
        <p>
          Atenção, o Balcão Virtual mantém o usuário autenticado por 18h após o
          <i>login</i>. Fechar o navegador não causa automaticamente um
          <i>logout</i>. Para forçar a desconexão do usuário, é necessário clicar na opção "Logout" do menu principal.
        </p>
  
        <div class="headline pb-1 pt-3">
          <h2>A Linha do Tempo</h2>
        </div>
        <p>
          <strong>Linha do Tempo do Processo</strong> é um diagrama que resume as ocorrências observadas em um processo judicial. Situações que ainda não ocorreram permanecem em cinza. Eventos detectados no histórico de movimentos são apresentados como círculos pretos e o caminho até eles é marcado de azul. Quando um mesmo evento ocorre mais de uma vez, o número total de ocorrências é apresentado dentro do respectivo círculo. O último evento detectado é marcado com um círculo laranja e ao seu lado podem ser apresentados alguns detalhes referentes, apenas, a esta última ocorrência.
        </p>
        <div class="headline pb-1 pt-3">
          <h2>Sobre o Balcão Virtual</h2>
        </div>
  
        <p>O Balcão Virtual foi desenvolvido em 2017 pela Coordenadoria de Inovação (CINOVA), parte da Secretaria de Tecnologia da Informação do Tribunal Regional Federal da 2ª Região, com a colaboração do Procurador Regional Substituto da Procuradoria Regional da Fazenda Nacional na 2ª Região, Dr. João Henrique Chauffaille Grognet.</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'sobre'
}
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>

</style>
