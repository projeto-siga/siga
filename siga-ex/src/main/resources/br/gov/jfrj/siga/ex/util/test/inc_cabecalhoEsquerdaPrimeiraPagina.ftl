<table width="100%" align="left" border="0">
	<tr>
		<td align="left" valign="bottom" width="15%"><img
			src="contextpath/imagens/brasao2.png" width="65" height="65" /></td>
		<td align="left" width="1%"></td>
		<td width="84%">
		<table align="left" width="100%">
			<tr>
				<td width="100%" align="left">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">PODER
				JUDICIÁRIO</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="left">
				<p style="font-family: Arial; font-size: 10pt; font-weight: bold;">JUSTIÇA
				FEDERAL</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="left">
					<p style="font-family: AvantGarde Bk BT, Arial; font-size: 8pt;">
						<#if mov??>
							${(mov.lotaTitular.orgaoUsuario.descricaoMaiusculas)!}
						<#else>
							${(doc.lotaTitular.orgaoUsuario.descricaoMaiusculas)!}
						</#if>
					</p>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
