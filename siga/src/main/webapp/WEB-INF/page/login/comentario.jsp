<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<div class="d-none d-md-block">
	<h2 class="gt-table-head">
		<b>Bem-vindo ao SIGA.</b>
	</h2>

	<c:if test="${f:resource('siga.base.url') == 'http://siga.jfrj.jus.br' or f:resource('siga.base.url') == 'https://siga.jfrj.jus.br' }">
		<hr/>
	
		<p style="font-size: 150%">Agora o Siga oferece a possibilidade de assinar documento com senha (sem token). Para isto, basta clicar na opção "Com Senha" na página de assinatura, e entrar com sua matrícula e a senha do Siga.</p>

		<hr/>
		
		<p style="font-size: 150%">Experimente a nova aplicação para acesso ao Siga via celular:
		<a href="https://siga.jfrj.jus.br/siga-le">${f:resource('siga.base.url')}/siga-le</a>
		</p>
		
		<p align="center"><img width="15%" src="/siga/imagens/qr-code-http-siga-jfrj-jus-br-siga-le.png"></img></p>
	</c:if>
	<hr/>
	<p style="font-size: 150%">Recomendamos o navegador Google Chrome para acesso ao SIGA.</p> </br>
	<a href="https://github.com/projeto-siga/siga" target="_blank" >Sobre o SIGA</a>
	<hr/>
	
</div>



<br> <c:if test="${param['completo'] eq 'true'}">

<h4>
	<br>NOVIDADES DA VERSÃO 6.0 
</h4>
<p>O SIGA-Doc, um dos principais módulos do sistema, incorpora novas
	funcionalidades que melhoram significativamente a racionalização da
	produção de documentos e a gestão de seu ciclo de vida. Principais
	inovações:</p>
<ul>
	<li>Na produção:
		<ul>
			<li>Documentos 100% digitais;</li>
			<li>Cadastrante como subscritor default;
				<li>Um único campo para escolha do documento a ser produzido,
					com uso de uma caixa de seleção hierárquica e busca textual;</li>
		</ul>
	</li>

	<li>Na tramitação do documento:
		<ul>
			<li>Simplificação dos ícones e das ações;</li>
			<li>Teclas de atalho para os principais links e botões;</li>
			<li>Inclusão de cópia de outros documentos do Siga-Doc;</li>
			<li>Único campo para inclusão de novos documentos;</li>
			<li>Despachos passam a receber numeração, o que possibilita a
				edição, o referenciamento, o desentranhamento e a cópia do
				documento;</li>
			<li>Nova página para assinatura de documento (inclusive
				temporário), com inclusão de trâmite imediato após assinatura;</li>
			<li>Criação da funcionalidade "Arquivos Auxiliares", que
				possibilita a inserção, o versionamento e a consulta de documentos
				em formatos que não sejam PDF e facultada a edição de arquivos do
				Microsoft Word, Excel e PowerPoint;</li>
		</ul>
	</li>

	<li>Documentos capturados:
		<ul>
			<li>Criada a possibilidade de edição;</li>
			<li>Inclusão em documentos já finalizados;</li>
			<li>Documentos externos podem ser recuperados a partir de seu
				número ou do nome do emitente;</li>
			<li>Identificação de emitente já cadastrado no sistema.</li>
		</ul>
	</li>
</ul>


<p>
	<br>Resultado final 
</p>
<ul>
	<li>Menor número de cliques;</li>
	<li>Menos campos para preenchimento;</li>
	<li>Maior precisão na recuperação dos documentos produzidos e
		recebidos;</li>
	<li>Maior satisfação dos usuários.</li>
</ul>

		<h3 class="gt-table-head">Veja abaixo as últimas novidades:</h3>

		<h4>Novos Módulos</h4>
		<p>O SIGA agora está integrado com os módulos do Siga SGP. Foram
			integrados: Cadastro, Benefícios, AQ e Lotações (fase final de
			testes). Podem ser acessados pelo menu, na seguinte opção: Módulos /
			Pessoas. Os módulos estarão disponíveis de acordo com as permissões
			dos usuários logados. Em breve, mais novidades.</p>

		<h4>Novo Design</h4>
		<p>O SIGA apresenta, agora, um design muito mais simples e
			moderno. Além das mudanças estéticas, também foram simplificadas
			algumas páginas e operações, o que beneficia principalmente as
			pessoas que utilizam muito o sistema.</p>

		<h4>Modelos Padronizados</h4>
		<p>Está sendo realizada uma padronização dos modelos de
			expedientes usados pelo CJF e pelos TRF's da 2ª e 3ª Região. Alguns
			modelos, como o de ofício, já estão com um novo layout no Siga-Doc.</p>

		<h4>Novo Editor de Textos</h4>
		<p>
			Siga-Doc possui um novo editor de textos, com mais recursos e menos
			problemas de formatação. Inicialmente, ele está disponível apenas em
			alguns modelos. Gradualmente, o antigo editor será substituído. <a
				href="/siga/arquivos/AjudaEditorTextosSigaDoc.pdf"><b>Veja
					instruções</b> </a> sobre o uso do editor.
		</p>

		<h4>Busca Textual Integrada</h4>
		<p>A busca de documentos por conteúdo agora pode ser feita na
			própria tela de pesquisa por filtros, por meio do campo Conteúdo.</p>


		<h4>Assinador da Certisign</h4>
		<p>
			Agora a assinatura digital pode ser feita por meio de um recurso
			desenvolvido pela Certisign. Basta clicar em <b>Assinar com
				assinador da Certisign</b>, na tela de assinatura. A tecnologia
			facilitará, principalmente, o uso do Siga-Doc com certificado digital
			em outros órgãos.
		</p>


		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Quadro de Solicitações</b><br> Assim
						como o módulo Documentos, o novo módulo Serviços também exibe um
						resumo na página inicial, dentro do <i>Quadro de Solicitações</i>.</font>
			</td>
		</tr>

		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>PCTT - nova versão</b><br> Está
						disponível para uso no sistema a nova tabela de classificação. O
						formato do código da classificação mudou para NN.NN.NN.NN. A
						maioria das classificações possui apenas uma via.</font></td>
		</tr>

		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Mudança na exibição do status</b><br>
						Na tela de visualização de documentos, o estado em que uma via se
						encontra agora é exibido mais acima, ao lado do número da via. <a
						style="font-weight: bold" href="/siga/arquivos/exibeEstado.htm"
						target="new">Veja a diferença na imagem</a>.</font></td>
		</tr>

		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Consulta a documentos</b><br> Agora
						é possível consultar documentos através do estado, incluindo <i>Transferido</i>
						e <i>Como Subscritor</i>. Basta selecionar a situação desejada na
						lista e, ao lado, a pessoa ou lotação a quem o estado se refere. <a
						style="font-weight: bold" href="/siga/arquivos/novaListaJsp.JPG"
						target="new">Veja a imagem</a>.</font></td>
		</tr>

		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Notificação de documentos a receber</b><br>
						É possível, por meio de chamado, especificar para quais endereços
						de e-mail o sistema deverá enviar notificação de transferência de
						documento eletrônico para uma determinada lotação.</font></td>
		</tr>

		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Documentos Eletrônicos</b><br> Agora
						todos os expedientes podem ser gerados e movimentados de forma
						totalmente digital, não necessitando ser impressos. Para gerar um
						documento eletrônico, marque a opção 'Digital', na parte superior
						da tela de edição do expediente.</font></td>
		</tr>


		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Pesquisa por Documentos Sigilosos</b><br>
						A busca textual abrange os documentos com nível de acesso
						restrito. Para incluí-los na pesquisa, marque a opção localizada
						abaixo do campo de busca.</font></td>
		</tr>

		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Visualizar Dossiê</b><br> O recurso
						Visualizar Dossiê permite navegar mais facilmente pelos despachos,
						documentos filhos e anexos de um expediente, bem como visualizar
						ou imprimir seus conteúdos como um único documento.</font></td>
		</tr>

		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Mudanças na nomenclatura</b><br>
						Seguem os nomes antigos e seus equivalentes após a alteração na
						nomenclatura:
						<ul>
							<li>Em andamento: Aguardando Andamento</li>
							<li>Em Trânsito: Transferido</li>
							<li>Arquivado Corrente: Arquivo Corrente</li>
							<li>Cancelar Movimentação: Desfazer + <i>Nome da Ação</i></li>
						</ul></font></td>
		</tr>

		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Caixa "Ir para o documento"</b><br>
						O campo de busca por código de expediente agora está disponível
						também nas páginas de listagem e de exibição de documentos.</font></td>
		</tr>

		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Despacho Longo Diferente</b><br> Ao
						fazer um despacho longo, o usuário vê a tela para edição do
						despacho como documento filho, um expediente à parte. Quando este
						é finalizado e assinado, ocorre uma juntada automática ao pai.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Substituição Mais Fácil</b><br> Para
						mudar de perfil, basta passar o mouse sobre o seu nome, no rodapé,
						e selecionar a pessoa ou lotação desejada.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Salvamento Automático</b><br> Os
						documentos são agora salvos automaticamente, a cada dois minutos.
						Foi incluído também o botão 'Salvar' na barra de ferramentas do
						editor de textos.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Consulta Por Intervalo de Datas</b><br>
						A busca por documentos pode agora ser feita não só por uma data
						fixa, mas tambem por intervalo de datas.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Arquivamento em Lote</b><br> Está
						disponível o recurso que permite fazer o arquivamento corrente
						simultâneo de vários expedientes.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Dedicada aos Subscritores</b><br> Há
						uma nova informação na tabela de contagem de expedientes da página
						inicial. A linha "Como Subscritor" dá acesso a todos os documentos
						que necessitam ser assinados por quem está operando o sistema.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Incrementada a Busca Por
						Classificação</b><br> Agora todas as colunas da <a
						href="http://intranet/conteudos/gestao_documental/gestao_documental.asp"
						style="color: blue">Tabela de Temporalidade</a> são mostradas na
						busca por classificação do SIGA-EX.</font></td>
		</tr>

		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Pesquisa de Expedientes por
						Cadastrante</b><br> Foi adicionado o campo "Cadastrante" à tela
						de busca por expedientes. Assim, pode-se listar os documentos
						feitos por uma pessoa ou lotação.</font></td>
		</tr>



		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Relação de Paternidade Entre
						Documentos</b><br> Está disponível a opção "Criar Documento
						Filho", útil principalmente para facilitar a geração de resposta a
						um expediente. Quando assinado, um documento filho é
						automaticamente juntado ao pai. É possivel também, na criação de
						um expediente, definir qual o seu documento pai.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Novos Links na Página Inicial</b><br>
						A àrea lateral esquerda da página principal foi reformulada, e
						estão disponíveis links para a tabela de temporalidade, para a
						apostila do SIGA-EX, entre outros.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Expedientes Mais Protegidos</b><br>
						Os documentos só se tornam visíveis às lotações não atendentes
						depois de assinados.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Assinar É Necessário</b><br> Para
						transferir um documento, agora é preciso antes assiná-lo, a não
						ser que a lotação à qual se envia expresse, por chamado, aceitar
						documentos não assinados.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Documentos Digitais</b><br> Cerca de
						um quinto dos documentos criados hoje são totalmente digitais, e o
						número tende a aumentar. Leia o <a
						href="/siga/arquvios/roteiro_eletronicos.pdf" style="color: blue">roteiro
							para assinatura digital</a>.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Pesquisa Textual Mais Completa</b><br>
						Agora a busca textual abrange também os despachos, os anexos e as
						anotações.</font></td>
		</tr>

		<tr>
			<td
				style="border-top: 1px dotted #CCCCCC; padding-top: 7; padding-bottom: 7"><font
				size='2' align="justify"> <b>Pesquisa Textual</b><br>
						Experimente o novo sistema de busca textual de documentos do
						SIGA-EX: no menu principal, clique em "Expedientes" e depois
						"Pesquisar por texto".</font></td>
		</tr>

		<tr>
			<td
				style="border-top: 1px dotted #CCCCCC; padding-top: 7; padding-bottom: 7"><font
				size='2' align="justify"> <b>Menus</b><br> Agora o SIGA
						possui um sistema de menus hierárquicos, permitindo assim uma
						navegação mais fácil e ágil.</font></td>
		</tr>


		<tr>
			<td
				style="border-top: 1px dotted #CCCCCC; padding-top: 7; padding-bottom: 7"><font
				size='2' align="justify"> <b>Alteração na Nomeclatura dos
						Tipos de Documentos</b><br> Os tipos de documento "Interno" e
						"Interno Antigo" passam a se chamar respectivamente: "Interno
						Produzido" e "Interno Importado".</font></td>
		</tr>
		<tr>
			<td
				style="border-top: 1px dotted #CCCCCC; padding-top: 7; padding-bottom: 7"><font
				size='2' align="justify"><b>Formulários Eletrônicos</b> <br>
						Os formulários de Substituição, Designação e Dispensa, Remoção e
						Permuta já podem ser utilizados eletronicamente.</font></td>
		</tr>
		<tr>
			<td
				style="border-top: 1px dotted #CCCCCC; padding-top: 7; padding-bottom: 7"><font
				size='2' align="justify"><b>Novos Formulários</b> <br>
						Foram criados novos formulários de: <u>Boletim de Frequência</u>,
						Parcelamento de Débito, Termo de Compromisso e Recebimento do
						Crachá de Identificação Funcional e Declaração de Bens.</font></td>
		</tr>

	</c:if> </font>
	</table>