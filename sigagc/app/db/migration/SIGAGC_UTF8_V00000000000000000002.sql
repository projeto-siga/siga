

ALTER SESSION SET CURRENT_SCHEMA = sigagc;

alter table sigagc.gc_tipo_informacao add arquivo number (19,0) constraint TIPO_INFORMACAO_ARQUIVO_FK references gc_arquivo
--

insert into sigagc.gc_arquivo (id_conteudo, conteudo_tipo, titulo, conteudo) values ((select max(id_conteudo)+1 from sigagc.gc_arquivo), 'text/html', 'Template para Erro Conhecido', utl_raw.cast_to_raw(' '));
insert into GC_TIPO_INFORMACAO (ID_TIPO_INFORMACAO, NOME_TIPO_INFORMACAO, arquivo) values (2, 'Erro Conhecido', (select max(id_conteudo) from sigagc.gc_arquivo))
commit;

set define off
DECLARE
  dest_blob BLOB;
  src_blob BLOB;
BEGIN

select conteudo into dest_blob from sigagc.gc_arquivo where id_conteudo = (select max(id_conteudo) from sigagc.gc_arquivo) for update;
src_blob := utl_raw.cast_to_raw(convert('<h2><u>T&iacute;tulo</u></h2>
<p>Identificar de forma sucinta o sintoma apresentado pelo erro. O t&iacute;tulo ser&aacute; exibido automaticamente ao atendente da Central ap&oacute;s a classifica&ccedil;&atilde;o do chamado (Servi&ccedil;o/a&ccedil;&atilde;o).</p>
<h2><u>Sintoma</u></h2>
<p>Descrever o que &eacute; visualizado pelo usu&aacute;rio como sendo o problema (Respectiva msg de erro, comportamento do ambiente, exemplo de tela, etc).</p>
<h2><u>Causa</u></h2>
<p>Descrever a causa j&aacute; identificada para o sintoma descrito.</p>
<h2><u>Resolu&ccedil;&atilde;o</u></h2>
<p><b>Procedimento a ser executado pela Central de Servi&ccedil;os para contornar, sanar ou escalonar o chamado:</b></p>
<p style="color: red">Layout A (Utilizar caso o passo a passo seja diferente, dependendo do &oacute;rg&atilde;o)</p>
<table cellpadding="0" cellspacing="0" style="border-width: 1px; border-style: solid;">
	<tr>
		<td style="border-style: solid; border-width: 0px; border-right-width: 1px; border-bottom-width: 1px;">SJRJ</td>
		<td style="border-style: solid; border-width: 0px; border-right-width: 1px; border-bottom-width: 1px;">TRF</td>
		<td style="border-style: solid; border-width: 0px; border-bottom-width: 1px;">SJES</td>
	</tr>
	<tr>
		<td style="border-style: solid; border-width: 0px; border-right-width: 1px; padding-right: 10px;">
			<ol style="margin-left: 0px">
				<li></li>
				<li></li>
			</ol>
		</td>
		<td style="border-style: solid; border-width: 0px; border-right-width: 1px; padding-right: 10px;">
			<ol style="margin-left: 0px">
				<li></li>
				<li></li>
			</ol>
		</td>
		<td style="border-style: solid; border-width: 0px; padding-right: 10px;">
			<ol style="margin-left: 0px">
				<li></li>
				<li></li>
			</ol>
		</td>
	</tr>
</table>
<p style="color: red">Layout B (Utilizar quando o procedimento &eacute; o mesmo, independente do &oacute;rg&atilde;o)</p>
<ol>
	<li></li>
	<li></li>
</ol>
<p><b>Procedimento a ser executado por outros Grupos Solucionadores:</b></p>
<p style="color: red">(Utilizar o Layout A acima, caso sejam procedimentos distintos para cada &oacute;rg&atilde;o)</p>
<ol>
	<li></li>
	<li></li>
</ol>
<h2><u>Se aplica para:</u></h2>
<p>Relacionar os ambientes em que a resolu&ccedil;&atilde;o descrita se aplica (Vers&otilde;es de sistemas operacionais, browsers, etc).</p>
<h2><u>Palavras para pesquisa:</u></h2>
<p>Relacionar termos que podem facilitar a pesquisa/localiza&ccedil;&atilde;o deste artigo na Base de Conhecimento.</p>','WE8ISO8859P1'));
dbms_lob.append(dest_blob, src_blob);
commit;
end;

--------------------------------------------------------------

insert into sigagc.gc_arquivo (id_conteudo, conteudo_tipo, titulo, conteudo) values ((select max(id_conteudo)+1 from sigagc.gc_arquivo), 'text/html', 'Template para Procedimento', utl_raw.cast_to_raw(' '));
insert into GC_TIPO_INFORMACAO (ID_TIPO_INFORMACAO, NOME_TIPO_INFORMACAO, arquivo) values (3, 'Procedimento', (select max(id_conteudo) from sigagc.gc_arquivo))
commit;

set define off
DECLARE
  dest_blob BLOB;
  src_blob BLOB;
BEGIN

select conteudo into dest_blob from sigagc.gc_arquivo where id_conteudo = (select max(id_conteudo) from sigagc.gc_arquivo) for update;
src_blob := utl_raw.cast_to_raw(convert('<h2><u>T&iacute;tulo</u></h2>
<p>Descrever de forma sucinta o procedimento abordado no artigo.</p>
<h2><u>Informa&ccedil;&otilde;es</u></h2>
<p>Descrever informa&ccedil;&otilde;es relevantes quanto ao procedimento documentado.</p>
<h2><u>Procedimento</u></h2>
<p>Procedimento a ser executado pela Central de Servi&ccedil;os:</p>
<p style="color: red">Layout A (Utilizar caso o passo a passo seja diferente, dependendo do &oacute;rg&atilde;o)</p>
<table cellpadding="0" cellspacing="0" style="border-width: 1px; border-style: solid;">
	<tr>
		<td style="border-style: solid; border-width: 0px; border-right-width: 1px; border-bottom-width: 1px;">SJRJ</td>
		<td style="border-style: solid; border-width: 0px; border-right-width: 1px; border-bottom-width: 1px;">TRF</td>
		<td style="border-style: solid; border-width: 0px; border-bottom-width: 1px;">SJES</td>
	</tr>
	<tr>
		<td style="border-style: solid; border-width: 0px; border-right-width: 1px; padding-right: 10px;">
			<ol style="margin-left: 0px">
				<li></li>
				<li></li>
			</ol>
		</td>
		<td style="border-style: solid; border-width: 0px; border-right-width: 1px; padding-right: 10px;">
			<ol style="margin-left: 0px">
				<li></li>
				<li></li>
			</ol>
		</td>
		<td style="border-style: solid; border-width: 0px; padding-right: 10px;">
			<ol style="margin-left: 0px">
				<li></li>
				<li></li>
			</ol>
		</td>
	</tr>
</table>
<p style="color: red">Layout B (Utilizar quando o procedimento &eacute; o mesmo, independente do &oacute;rg&atilde;o)</p>
<ol>
	<li></li>
	<li></li>
</ol>
<p><b>Procedimento a ser executado por outros Grupos Solucionadores:</b></p>
<p style="color: red">(Utilizar o Layout A acima, caso sejam procedimentos distintos para cada &oacute;rg&atilde;o)</p>
<ol>
	<li></li>
	<li></li>
</ol>
<h2><u>Se aplica para:</u></h2>
<p>Relacionar os ambientes em que o procedimento pode ser aplicado (Vers&otilde;es de sistemas operacionais, browsers, etc).</p>
<h2><u>Palavras para pesquisa:</u></h2>
<p>Relacionar termos que podem facilitar a pesquisa/localiza&ccedil;&atilde;o deste artigo na Base de Conhecimento.</p>','WE8ISO8859P1'));
dbms_lob.append(dest_blob, src_blob);
commit;
end;
