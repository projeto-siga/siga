<%@ include file="/WEB-INF/page/include.jsp"%>

<c:if test="${informacao.contemArquivos}">
	<h3 style="padding-top: 1em">Incluir Imagens ou Arquivos no Texto</h3>
	<p>Clique em uma imagem/arquivo abaixo para incluir uma referência
		no texto.</p>
	<c:forEach var="m" items="${informacao.movs}">
		<c:if
			test="${m.tipo.id == 13 and m.movCanceladora == null}">
			<p>
				<img style="margin-bottom: -4px;"
					src="/siga/css/famfamfam/icons/${m.arq.icon}.png" /> <a
					style="padding-right: 5px;"
					href="javascript: var frm = document.getElementById('frm'); insertImageAtCursor(frm.conteudo, '<c:choose><c:when test="${m.arq.image}">{{${m.arq.id}|${m.arq.titulo}}}</c:when>
					<c:otherwise>[[${m.arq.id}|${m.arq.titulo}]]</c:otherwise>
					</c:choose>');">${m.arq.titulo}
				</a> [ <img style="margin-bottom: -1px; width: 9px;"
					src="/siga/css/famfamfam/icons/cross.png" /> <span
					class="gt-table-action-list"> <a
					href="javascript:if (confirm('Confirma a remoção deste anexo?')) 
					ReplaceInnerHTMLFromAjaxResponse('../removerAnexo?sigla=${informacao.sigla}&idArq=${m.arq.id}&idMov=${m.id}',
										null, document.getElementById('ajax_arquivo'));">remover</a></span>
				&nbsp;]
			</p>
		</c:if>
	</c:forEach>
</c:if>