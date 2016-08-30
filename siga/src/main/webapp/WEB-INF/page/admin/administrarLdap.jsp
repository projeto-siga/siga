<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript" src="/siga/javascript/adminLDAP.js"></script>

<siga:pagina popup="false" titulo="Administração de integração ao LDAP">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<h1>Administração de integração ao LDAP</h1>
			
			<br>
						
			<h2>Órgão conectado ao LDAP</h2>
			<div class="gt-content-box gt-for-table">
					<table class="gt-form-table">
						<colgroup>
							<col width="15%"></col>
						</colgroup>

						<tr class="header">
							<td align="center" valign="top" colspan="4">
								Definir órgão conectado ao AD
							</td>
						</tr>

						<tr>
							<td >
								<label for="localidade">Sigla que identifica o órgão nas properties:</label>
								<input type="text" id="localidade" value="jfrj" placeholder="Exemplo: jfrj"></inpu>
							</td>
						</tr>

					</table>
			</div>
			<br>
			<h2>Propriedades definidas</h2>
			<div class="gt-content-box gt-for-table">
					<table class="gt-form-table">
						<colgroup>
							<col width="15%"></col>
						</colgroup>

						<tr class="header">
							<td align="center" valign="top" colspan="4">
								Propriedades definidas para a integração com LDAP
							</td>
						</tr>

						<tr>
							<td >
								<div class="resultado-propriedades"></div>
								<button class="gt-btn-medium gt-btn-right" onclick="listarPropriedadesLDAP()">Listar</button>
							</td>
						</tr>

					</table>
			</div>
	
			<br>
			<h2>Testar Conexão</h2>
			<div class="gt-content-box gt-for-table">
					<table class="gt-form-table">
						<colgroup>
							<col width="15%"></col>
						</colgroup>

						<tr class="header">
							<td align="center" valign="top" colspan="4">
								Testar a conexão com o LDAP
							</td>
						</tr>

						<tr>
							<td >
								<div class="resultado-testarConexao"></div>
								<button class="gt-btn-medium gt-btn-right" onclick="testarLDAP()">Testar</button>
							</td>
						</tr>

					</table>
			</div>
			
			<br>
			<h2>Testar JCE Strength</h2>
			<div class="gt-content-box gt-for-table">
					<table class="gt-form-table">
						<colgroup>
							<col width="15%"></col>
						</colgroup>

						<tr class="header">
							<td align="center" valign="top" colspan="4">
								Testar a força do JCE
							</td>
						</tr>

						<tr>
							<td >
								<div class="resultado-testarJCE"></div>
								<button class="gt-btn-medium gt-btn-right" onclick="testarJCE()">Testar</button>
							</td>
						</tr>

					</table>
			</div>
			
			<br>
			
			<h2>Trocar Senha</h2>
			<div class="gt-content-box gt-for-table">
					<table class="gt-form-table">
						<colgroup>
							<col width="15%"></col>
						</colgroup>

						<tr class="header">
							<td align="center" valign="top" colspan="4">
								Trocar a senha de um usuário no LDAP
							</td>
						</tr>

						<tr>
							<td>
								<label for="distinguishedName">Objeto (DN) [Caminho completo]</label>
							</td>
							<td>
								<input type="text" class="form-control typeahead" id="distinguishedName" placeholder="CN=RJ00000,OU=Usuarios,OU=Gestao de Identidade,DC=corp,DC=jfrj,DC=gov,DC=br">
							</td>
						</tr>
						<tr>
							<td>
								<label for="senha">Senha</label>
							</td>
							<td>
								<input type="password" class="form-control typeahead" id="password" placeholder="">
							</td>
						</tr>
						<tr>
							<td>
							</td>
							<td>
								<button class="gt-btn-medium gt-btn-right" onclick="trocarSenhaLDAP()">Trocar senha</button>
							</td>
						</tr>
						<tr>
							<td >
								<div class="resultado-trocarSenha"></div>
							</td>
						</tr>
						
					</table>
			</div>
			
			<br>
			
		</div>
	</div>			
			
</siga:pagina>


