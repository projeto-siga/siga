--Retirando título dos templates, pois já existe no conhecimento

set define off
DECLARE
  dest_blob BLOB;
  src_blob BLOB;
BEGIN

select conteudo into dest_blob from sigagc.gc_arquivo where id_conteudo = (select arquivo from sigagc.gc_tipo_informacao where id_tipo_informacao = 2) for update;
src_blob := utl_raw.cast_to_raw(convert('<h2><u>Se aplica para:</u></h2>
<p>Relacionar os ambientes em que a resolu&ccedil;&atilde;o descrita se aplica (Vers&otilde;es de sistemas operacionais, browsers, etc).</p>
<h2><u>Sintoma</u></h2>
<p>Descrever o que &eacute; visualizado pelo usu&aacute;rio como sendo o problema (Respectiva msg de erro, comportamento do ambiente, exemplo de tela, etc).</p>
<h2><u>Causa</u></h2>
<p>Descrever a causa j&aacute; identificada para o sintoma descrito.</p>
<h2><u>Resolu&ccedil;&atilde;o</u></h2>
<p><b>Procedimento a ser executado pela Central de Servi&ccedil;os para contornar, sanar ou escalonar o chamado:</b></p>
<p style="color: rgb(0,112,192)">Layout A (Utilizar caso o passo a passo seja diferente, dependendo do &oacute;rg&atilde;o, excluindo o Layout B)</p>
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
<p style="color: rgb(0,112,192)">Layout B (Utilizar quando o procedimento &eacute; o mesmo, independente do &oacute;rg&atilde;o, excluindo o Layout A)</p>
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
<h2><u>Solu&ccedil;&atilde;o de contorno</u></h2>
<p>Descrever, se for o caso, procedimento que pode ser adotado provisoriamente para viabilizar a continuidade do trabalho, enquanto n&atilde;o for aplicada uma solu&ccedil;&atilde;o definitiva.</p>
<h2><u>Palavras para pesquisa:</u></h2>
<p>Relacionar termos que podem facilitar a pesquisa/localiza&ccedil;&atilde;o deste artigo na Base de Conhecimento.</p>','WE8ISO8859P1'));
dbms_lob.write(dest_blob, length(src_blob), 1, src_blob);
commit;
end;




set define off
DECLARE
  dest_blob BLOB;
  src_blob BLOB;
BEGIN

select conteudo into dest_blob from sigagc.gc_arquivo where id_conteudo = (select arquivo from sigagc.gc_tipo_informacao where id_tipo_informacao = 3) for update;
src_blob := utl_raw.cast_to_raw(convert('<h2><u>Se aplica para:</u></h2>
<p>Relacionar os ambientes em que o procedimento pode ser aplicado (Vers&otilde;es de sistemas operacionais, browsers, etc).</p>
<h2><u>Informa&ccedil;&otilde;es</u></h2>
<p>Descrever informa&ccedil;&otilde;es relevantes quanto ao procedimento documentado.</p>
<h2><u>Procedimento</u></h2>
<p>Procedimento a ser executado pela Central de Servi&ccedil;os:</p>
<p style="color: rgb(0,112,192)">Layout A (Utilizar caso o passo a passo seja diferente, dependendo do &oacute;rg&atilde;o)</p>
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
<p style="color: rgb(0,112,192)">Layout B (Utilizar quando o procedimento &eacute; o mesmo, independente do &oacute;rg&atilde;o)</p>
<ol>
	<li></li>
	<li></li>
	<li></li>
</ol>
<p><b>Procedimento a ser executado por outros Grupos Solucionadores:</b></p>
<p style="color: rgb(0,112,192)">(Utilizar o Layout A acima, caso sejam procedimentos distintos para cada &oacute;rg&atilde;o)</p>
<ol>
	<li></li>
	<li></li>
	<li></li>
</ol>
<h2><u>Palavras para pesquisa:</u></h2>
<p>Relacionar termos que podem facilitar a pesquisa/localiza&ccedil;&atilde;o deste artigo na Base de Conhecimento.</p>','WE8ISO8859P1'));
dbms_lob.write(dest_blob, length(src_blob), 1, src_blob);
commit;
end;
