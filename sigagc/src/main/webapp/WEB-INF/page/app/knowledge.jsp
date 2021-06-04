<%@ include file="/WEB-INF/page/include.jsp"%>

<div class="gt-content-box gt-for-table">
	<table border="0" style="width: 100%" class="gt-table">
		<thead>
			<tr>
				<th style="text-align: left"><a
					style="float: right; margin-left: 15px; margin-bottom: 15px;"
					title="Editar a descrição"
					href="javascript: document.getElementById('desc_ver').style.display='none'; document.getElementById('desc_editar').style.display=''; document.getElementById('desc_but_editar').style.display='none'; document.getElementById('desc_but_gravar').style.display='';"><img
						src="/siga/css/famfamfam/icons/pencil.png"> </a>Informações</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><c:forEach var="conhecimento" items="${conhecimentos}">
						<div
							style="display: block; width: 33%; height: 8em; float: left; word-wrap: normal; white-space: wrap; overflow: hidden; text-overflow: ellipsis;">
							<h4 style="margin: 0; padding: 0; border: 0;">
								<a href="${linkTo[AppController].exibir(conhecimento[3])}">${conhecimento[1]}</a>
							</h4>
							<div style="padding: 0; border: 0;">${conhecimento[2]}</div>
						</div>
					</c:forEach></td>
			</tr>
		</tbody>
	</table>
</div>
