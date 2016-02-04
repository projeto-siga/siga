<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<p align="left" /><font size='3'><b>Bem-vindo ao SIGA.</b></font></p>
<p><font size='3' align="justify">Veja abaixo as últimas
novidades.</font></p>

<table>
	<tr>
		<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
			<img src="/sigaex/imagens/info.jpg" /></td>
			<td style="padding-top: 7; padding-bottom: 7">
			<font size='2' align="justify"> <b>Dedicada aos Subscritores</b><br>
		Há uma nova informação na tabela de contagem de expedientes da página
		inicial. A linha "Como Subscritor" dá acesso a todos os documentos que
		necessitam ser assinados por quem está operando o sistema.</font></td>
	</tr>
	<tr>
		<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
		<img src="/sigaex/imagens/search.jpg" /></td>
		<td style="padding-top: 7; padding-bottom: 7">
		<font size='2'
			align="justify"> <b>Incrementada a Busca Por Classificação</b><br>
		Agora todas as colunas da <a
			href="http://intranet/conteudos/gestao_documental/gestao_documental.asp"
			style="color: blue">Tabela de Temporalidade</a> são mostradas na
		busca por classificação do SIGA-EX.</font></td>
	</tr>
	
	<tr>
		<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
		<img src="/sigaex/imagens/page_search.jpg" /></td>
		<td style="padding-top: 7; padding-bottom: 7">
		<font size='2'
			align="justify"> <b>Pesquisa de Expedientes por
		Cadastrante</b><br>
		Foi adicionado o campo "Cadastrante" à tela de busca por expedientes.
		Assim, pode-se listar os documentos feitos por uma pessoa ou lotação.</font></td>
	</tr>
	<tr>
		<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
		<img src="/sigaex/imagens/applications.jpg" /></td>
		<td style="padding-top: 7; padding-bottom: 7">
		<font size='2'
			align="justify"> <b>Relação de Paternidade Entre
		Documentos</b><br>
		Está disponível a opção "Criar Documento Filho", útil principalmente
		para facilitar a geração de resposta a um expediente. Quando assinado,
		um documento filho é automaticamente juntado ao pai. É possivel
		também, na criação de um expediente, definir qual o seu documento pai.</font></td>
	</tr>
	<tr>
		<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
		<img src="/sigaex/imagens/info.jpg" /></td>
		<td style="padding-top: 7; padding-bottom: 7">
		<font size='2' align="justify"> <b>Novos Links na Página Inicial</b><br>
		A àrea lateral esquerda da página principal foi reformulada, e estão
		disponíveis links para a tabela de temporalidade, para a apostila do
		SIGA-EX, entre outros.</font></td>
	</tr>

	<c:if test="${param['completo'] eq 'true'}">
		<tr>
			<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
			<img src="/sigaex/imagens/application_lock.jpg" /></td>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Expedientes Mais Protegidos</b><br>
			Os documentos só se tornam visíveis às lotações não atendentes depois
			de assinados.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
			<img src="/sigaex/imagens/application_edit.jpg" /></td>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Assinar É Necessário</b><br>
			Para transferir um documento, agora é preciso antes assiná-lo, a não
			ser que a lotação à qual se envia expresse, por chamado, aceitar
			documentos não assinados.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
			<img src="/sigaex/imagens/application.jpg" /></td>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Documentos Digitais</b><br>
			Cerca de um quinto dos documentos criados hoje são totalmente
			digitais, e o número tende a aumentar. Leia o <a
				href="/siga/arquivos/roteiro_eletronicos.pdf" style="color: blue">roteiro para
			assinatura digital</a>.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
			<img src="/sigaex/imagens/page_search.jpg" /></td>
			<td style="padding-top: 7; padding-bottom: 7"><font size='2'
				align="justify"> <b>Pesquisa Textual Mais Completa</b><br>
			Agora a busca textual abrange também os despachos, os anexos e as
			anotações.</font></td>
		</tr>

		<tr>
			<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
			<img src="/sigaex/imagens/page_search.jpg" /></td>
			<td
				style="padding-top: 7; padding-bottom: 7"><font
				size='2' align="justify"> <b>Pesquisa Textual</b><br>
			Experimente o novo sistema de busca textual de documentos do SIGA-EX:
			no menu principal, clique em "Expedientes" e depois "Pesquisar por
			texto".</font></td>
		</tr>

		<tr>
			<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
			<img src="/sigaex/imagens/bulleted_list_002.jpg" height="45" width="45"/></td>
			<td
				style="padding-top: 7; padding-bottom: 7"><font
				size='2' align="justify"> <b>Menus</b><br>
			Agora o SIGA possui um sistema de menus hierárquicos, permitindo
			assim uma navegação mais fácil e ágil.</font></td>
		</tr>


		<tr>
			<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
			<img src="/sigaex/imagens/info.jpg" /></td>
			<td
				style="padding-top: 7; padding-bottom: 7"><font
				size='2' align="justify"> <b>Alteração na Nomeclatura dos
			Tipos de Documentos</b><br>
			Os tipos de documento "Interno" e "Interno Antigo" passam a se chamar
			respectivamente: "Interno Produzido" e "Interno Importado".</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
			<img src="/sigaex/imagens/format-justify-left.jpg" height="45" width="43"/></td>
			<td
				style="padding-top: 7; padding-bottom: 7"><font
				size='2' align="justify"><b>Formulários Eletrônicos</b> <br>
			Os formulários de Substituição, Designação e Dispensa, Remoção e
			Permuta já podem ser utilizados eletronicamente.</font></td>
		</tr>
		<tr>
			<td style="padding-top: 7; padding-bottom: 7" width="8%" align="center">
			<img src="/sigaex/imagens/format-justify-left.jpg" height="45" width="43"/></td>
			<td
				style="padding-top: 7; padding-bottom: 7"><font
				size='2' align="justify"><b>Novos Formulários</b> <br>
			Foram criados novos formulários de: <u>Boletim de Frequência</u>,
			Parcelamento de Débito, Termo de Compromisso e Recebimento do Crachá
			de Identificação Funcional e Declaração de Bens.</font></td>
		</tr>

	</c:if>
	</font>
</table>
