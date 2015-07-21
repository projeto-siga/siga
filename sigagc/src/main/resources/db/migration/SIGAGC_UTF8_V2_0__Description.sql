insert into sigagc.gc_arquivo (id_conteudo, conteudo_tipo, titulo, conteudo) values ((select NVL(max(id_conteudo), 1) from sigagc.gc_arquivo), 'text/html', 'Template para Erro Conhecido', utl_raw.cast_to_raw(' '));
insert into GC_TIPO_INFORMACAO (ID_TIPO_INFORMACAO, NOME_TIPO_INFORMACAO, arquivo) values (2, 'Erro Conhecido', (select max(id_conteudo) from sigagc.gc_arquivo));
commit;

DECLARE
  dest_blob BLOB;
  src_blob BLOB;
BEGIN

select conteudo into dest_blob from sigagc.gc_arquivo where id_conteudo = (select max(id_conteudo) from sigagc.gc_arquivo) for update;
src_blob := utl_raw.cast_to_raw(convert('<h2><u>T' || chr(38) || 'iacute;tulo</u></h2>
<p>Identificar de forma sucinta o sintoma apresentado pelo erro. O t' || chr(38) || 'iacute;tulo ser' || chr(38) || 'aacute; exibido automaticamente ao atendente da Central ap' || chr(38) || 'oacute;s a classifica' || chr(38) || 'ccedil;' || chr(38) || 'atilde;o do chamado (Servi' || chr(38) || 'ccedil;o/a' || chr(38) || 'ccedil;' || chr(38) || 'atilde;o).</p>
<h2><u>Se aplica para:</u></h2>
<p>Relacionar os ambientes em que a resolu' || chr(38) || 'ccedil;' || chr(38) || 'atilde;o descrita se aplica (Vers' || chr(38) || 'otilde;es de sistemas operacionais, browsers, etc).</p>
<h2><u>Sintoma</u></h2>
<p>Descrever o que ' || chr(38) || 'eacute; visualizado pelo usu' || chr(38) || 'aacute;rio como sendo o problema (Respectiva msg de erro, comportamento do ambiente, exemplo de tela, etc).</p>
<h2><u>Causa</u></h2>
<p>Descrever a causa j' || chr(38) || 'aacute; identificada para o sintoma descrito.</p>
<h2><u>Resolu' || chr(38) || 'ccedil;' || chr(38) || 'atilde;o</u></h2>
<p><b>Procedimento a ser executado pela Central de Servi' || chr(38) || 'ccedil;os para contornar, sanar ou escalonar o chamado:</b></p>
<p style="color: rgb(0,112,192)">Layout A (Utilizar caso o passo a passo seja diferente, dependendo do ' || chr(38) || 'oacute;rg' || chr(38) || 'atilde;o, excluindo o Layout B)</p>
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
<p style="color: rgb(0,112,192)">Layout B (Utilizar quando o procedimento ' || chr(38) || 'eacute; o mesmo, independente do ' || chr(38) || 'oacute;rg' || chr(38) || 'atilde;o, excluindo o Layout A)</p>
<ol>
	<li></li>
	<li></li>
</ol>
<p><b>Procedimento a ser executado por outros Grupos Solucionadores:</b></p>
<p style="color: rgb(0,112,192)">Layout A</p>
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
<p style="color: rgb(0,112,192)">Layout B</p>
<ol>
	<li></li>
	<li></li>
</ol>
<h2><u>Solu' || chr(38) || 'ccedil;' || chr(38) || 'atilde;o de contorno</u></h2>
<p>Descrever, se for o caso, procedimento que pode ser adotado provisoriamente para viabilizar a continuidade do trabalho, enquanto n' || chr(38) || 'atilde;o for aplicada uma solu' || chr(38) || 'ccedil;' || chr(38) || 'atilde;o definitiva.</p>
<h2><u>Palavras para pesquisa:</u></h2>
<p>Relacionar termos que podem facilitar a pesquisa/localiza' || chr(38) || 'ccedil;' || chr(38) || 'atilde;o deste artigo na Base de Conhecimento.</p>','WE8ISO8859P1'));
dbms_lob.append(dest_blob, src_blob);
commit;
end; 
/

--------------------------------------------------------------

insert into sigagc.gc_arquivo (id_conteudo, conteudo_tipo, titulo, conteudo) values ((select max(id_conteudo)+1 from sigagc.gc_arquivo), 'text/html', 'Template para Procedimento', utl_raw.cast_to_raw(' '));
insert into GC_TIPO_INFORMACAO (ID_TIPO_INFORMACAO, NOME_TIPO_INFORMACAO, arquivo) values (3, 'Procedimento', (select max(id_conteudo) from sigagc.gc_arquivo));
commit;

DECLARE
  dest_blob BLOB;
  src_blob BLOB;
BEGIN

select conteudo into dest_blob from sigagc.gc_arquivo where id_conteudo = (select max(id_conteudo) from sigagc.gc_arquivo) for update;
src_blob := utl_raw.cast_to_raw(convert('<h2><u>T' || chr(38) || 'iacute;tulo</u></h2>
<p>Descrever de forma sucinta o procedimento abordado no artigo.</p>
<h2><u>Se aplica para:</u></h2>
<p>Relacionar os ambientes em que o procedimento pode ser aplicado (Vers' || chr(38) || 'otilde;es de sistemas operacionais, browsers, etc).</p>
<h2><u>Informa' || chr(38) || 'ccedil;' || chr(38) || 'otilde;es</u></h2>
<p>Descrever informa' || chr(38) || 'ccedil;' || chr(38) || 'otilde;es relevantes quanto ao procedimento documentado.</p>
<h2><u>Procedimento</u></h2>
<p>Procedimento a ser executado pela Central de Servi' || chr(38) || 'ccedil;os:</p>
<p style="color: rgb(0,112,192)">Layout A (Utilizar caso o passo a passo seja diferente, dependendo do ' || chr(38) || 'oacute;rg' || chr(38) || 'atilde;o)</p>
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
<p style="color: rgb(0,112,192)">Layout B (Utilizar quando o procedimento ' || chr(38) || 'eacute; o mesmo, independente do ' || chr(38) || 'oacute;rg' || chr(38) || 'atilde;o)</p>
<ol>
	<li></li>
	<li></li>
	<li></li>
</ol>
<p><b>Procedimento a ser executado por outros Grupos Solucionadores:</b></p>
<p style="color: rgb(0,112,192)">(Utilizar o Layout A acima, caso sejam procedimentos distintos para cada ' || chr(38) || 'oacute;rg' || chr(38) || 'atilde;o)</p>
<ol>
	<li></li>
	<li></li>
	<li></li>
</ol>
<h2><u>Palavras para pesquisa:</u></h2>
<p>Relacionar termos que podem facilitar a pesquisa/localiza' || chr(38) || 'ccedil;' || chr(38) || 'atilde;o deste artigo na Base de Conhecimento.</p>','WE8ISO8859P1'));
dbms_lob.append(dest_blob, src_blob);
commit;
end; 
/