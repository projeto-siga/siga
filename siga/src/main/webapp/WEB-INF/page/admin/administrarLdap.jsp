<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript" src="/siga/javascript/adminLDAP.js"></script>

<siga:pagina popup="false" titulo="Administração de integração ao LDAP">
	<div class="container-fluid">
	
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>Definir órgão conectado ao AD</h5></div>
			<div class="card-body">
				<div class="row">
					<div class="col-sm-5">
						<div class="form-group">
							<label for="localidade">Sigla que identifica o órgão nas properties</label>
							<input class="form-control" type="text" id="localidade" value="jfrj" placeholder="Exemplo: jfrj"></input>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>Propriedades definidas para a integração com LDAP</h5></div>
			<div class="card-body">
				<div class="row">
					<div class="col">
						<div class="form-group">
							<div class="resultado-propriedades"></div>
							<button class="btn btn-primary" onclick="listarPropriedadesLDAP()">Listar</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>Testar a conexão com o LDAP</h5></div>
				<div class="card-body">
				<div class="row">
					<div class="col">
						<div class="form-group">			
							<div class="resultado-testarConexao"></div>
							<button class="btn btn-primary" onclick="testarLDAP()">Testar</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>Testar a força do JCE</h5></div>
			<div class="card-body">
				<div class="row">
					<div class="col">
						<div class="form-group">
							<div class="resultado-testarJCE"></div>
							<button class="btn btn-primary" onclick="testarJCE()">Testar</button>
						</div>
					</div>
				</div>
			</div>
		</div>
			
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>Trocar a senha de um usuário no LDAP</h5></div>
			<div class="card-body">
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">	
						<label for="distinguishedName">Objeto (DN) [Caminho completo]</label>
						<input type="text" class="form-control typeahead" id="distinguishedName" placeholder="CN=RJ00000,OU=Usuarios,OU=Gestao de Identidade,DC=corp,DC=jfrj,DC=gov,DC=br">
					</div>
				</div>
				<div class="col-sm-6">	
					<div class="form-group">	
						<label for="senha">Senha</label>
						<input type="password" class="form-control typeahead" id="password" placeholder="">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col">
					<div class="form-group">
						<button class="btn btn-primary" onclick="trocarSenhaLDAP()">Trocar senha</button>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col">
					<div class="form-group">
						<div class="resultado-trocarSenha"></div>
					</div>
				</div>
			</div>
		</div>
	</div>			
</siga:pagina>


