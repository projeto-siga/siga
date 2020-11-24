<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>


<div class="form-group">
	<label>
		${solicitacao.descrItemAtual}
		-
		${solicitacao.descrAcaoAtual}
	</label>
	<p id="descricaoSolicitacao" class="form-control">${solicitacao.descricao}</p>
	<script language="javascript">

		function formatStr(str, n) {
		   var strTmp = [], start=0;
		   while(start<str.length) {
			   strTmp.push(str.slice(start, start+n));
		      start+=n;
		   }
		   return strTmp.join(" ");
		};
		
		function parseDescricao(id){
			var descricao = document.getElementById(id);
			if (!descricao)
				return;
			
		    var wordsArray = descricao.innerHTML.split(/(\s+)/);
		    var temStringLonga = false;
			for(var i=0; i < wordsArray.length; i++){						
	    		if (wordsArray[i].length > 50 && wordsArray[i].substr(0,7) != "http://" && wordsArray[i].substr(0,8) != "https://") {
	    			wordsArray[i] = formatStr(wordsArray[i],60);
	    			temStringLonga = true;	    			
				}
			}
			if(temStringLonga){
				descricao.innerHTML = wordsArray.join(" ");
			}																			
			descricao.innerHTML = descricao.innerHTML.replace(/\n\r?/g, ' <br />');
			descricao.innerHTML = descricao.innerHTML.replace(/(\w{2,4}\-(GC|SR)\-\d{4}\/\d{5}(?:\.\d{2})?)/g, function(a, b, c){
				if (c.toLowerCase() == 'sr')
					return '<a href=\'/sigasr/app/solicitacao/exibir/'+ a.replace('/','') + '\'>' + a + '</a>';
				else
					return '<a href=\'/sigagc/app/exibir/' + a.replace('/','') + '\'>' + a + '</a>';;
			});
			descricao.innerHTML = descricao.innerHTML.replace(/(https?:\/\/.*?(?=[.,;""''?!)]*$|[.,;""''?!)]*[\s\n]))/g, function(a, b){
				return '<a href="' + b + '">' + b + '</a>' + a.replace(b, '');
			});
		}
		parseDescricao('descricaoSolicitacao');
	</script>
	
	<c:forEach items="${atributos}" var="att">
		<c:if test="${att.valorAtributoSolicitacao != null && !att.valorAtributoSolicitacao.isEmpty()}">
			<div>
				<p style="float: left; font-size: 9pt; padding: 0px"><b>${att.atributo.nomeAtributo}: &nbsp</b></p>
				<div class="atributo-editavel">
					<c:if test="${solicitacao.podeEditarAtributo(titular, lotaTitular)}">
						<div class="gt-table-action-list" style="float:right;">
							<a href="#" onclick="editarAtributo('${att.id}', '${att.atributo.tipoAtributo.name()}', '${att.atributo.preDefinidoSet}');">
								<img src="/siga/css/famfamfam/icons/pencil.png" title="editar" style="width: 40%; margin-left: 20px;" />
							</a>		
							<span class="gt-separator">|</span>			
							<a href="#" onclick="excluirAtributo('${att.id}', '${att.atributo.nomeAtributo}');">
								<img src="/siga/css/famfamfam/icons/delete.png" title="excluir" style="width: 80%;"/>
							</a>
						</div>
					</c:if>
					<p style="font-size: 9pt;">
						<span class="valor-atributo">${att.valorAtributoSolicitacao}</span>
					</p>
				</div>
			</div>
		</c:if>
	</c:forEach>
</div>