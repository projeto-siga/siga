package sigaidp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.gov.jfrj.siga.base.SigaHTTP;

public class SigaHTTPTest {
	
	SigaHTTP http;
	
	@Before
	public void init(){
		http = new SigaHTTP();
	}
	
	@Test
	public void deveriaIdentificarPaginaDoSIGAIDP() {
		assertTrue(http.isIDPPage(HTML_IDP));
	}

	@Test
	public void deveriaIdentificarPaginaDeLogin(){
		assertTrue(http.isAuthPage(HTML_INITIAL_PAGE));
	}
	
	String HTML_IDP = 
			"<html>"+
				"<head>"+
				"    <title>SIGA - Provedor de identidades</title>"+
				"    <link rel='stylesheet' href='css/main.css' type='text/css' media='screen' />"+
				"</head>"+
				"<body>"+
				"    <div id='siga-box'>"+
				"        <div id='descricao'>"+
				"            Escolha o m&oacute;dulo do SIGA que deseja acessar"+
				"        </div>"+
				"        <div id='siga-modules'>"+
				"            <a href='/siga' class='button'>SigaDOC<i class='icon-chevron-right'></i></a>"+
				"            <a href='/sigarh' class='button'>SigaRH<i class='icon-chevron-right'></i></a>"+
				"        </div>"+
				"    </div>"+
				"</body>"+
			"</html>";
	
	String HTML_INITIAL_PAGE = 
			"html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en' lang='en'>"+
					"<head>"+
					"<title>SIGA - Justiça Federal</title>"+
					"<meta http-equiv='X-UA-Compatible' content='IE=EmulateIE9'/>"+
					"<META HTTP-EQUIV='Expires' CONTENT='0'>"+
					"<META HTTP-EQUIV='Pragma' CONTENT='no-cache'>"+
					"<META HTTP-EQUIV='Cache-Control' CONTENT='no-cache'>"+
					"<META HTTP-EQUIV='content-type' CONTENT='text/html; charset=UTF-8'>"+
					"<link rel='stylesheet' href='/siga/css/ecoblue/css/reset-fonts.css' type='text/css' media='screen, projection'>"+
					"<link rel='stylesheet' href='/siga/css/ecoblue/css/gt-styles.css' type='text/css' media='screen, projection'>"+
					"<link rel='stylesheet' href='/siga/css/ecoblue/css/custom.css' type='text/css' media='screen, projection'>"+
					"<!-- <link rel='StyleSheet' href='/sigalibs/siga.css' type='text/css'	title='SIGA Estilos' media='screen'> -->"+
					"<script src='/siga/sigalibs/ajax.js' type='text/javascript'></script>"+
					"<script src='/siga/sigalibs/static_javascript.js' type='text/javascript' charset='utf-8'></script>"+
					"<script src='/siga/javascript/picketlink.js' type='text/javascript' charset='utf-8'></script>"+
					"<!-- <link href='/sigaidp/sigalibs/menu.css'"+
					"	rel='stylesheet' type='text/css' /> -->"+
					"<link rel='shortcut icon' href='/siga/sigalibs/siga.ico' />"+
					"<!-- "+
					"<script src='/siga/public/javascript/jquery/jquery-1.11.2.min.js' type='text/javascript'></script>"+
					"<script src='/siga/javascript/jquery/jquery-migrate-1.2.1.min.js' type='text/javascript'></script>"+
					"-->"+
					"<script src='/siga/javascript/jquery/1.6/jquery-1.6.4.min.js' type='text/javascript'></script>"+
					"<script src='/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js' type='text/javascript'></script>"+
					"<script src='/siga/javascript/json2.js' type='text/javascript'></script>"+
					"<link rel='stylesheet' href='/siga/javascript/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.min.css' type='text/css' media='screen, projection'>"+
					"<!-- <link rel='stylesheet' href='/siga/javascript/jquery-ui-1.10.3.custom/development-bundle/themes/base/jquery.ui.all.css'"+
					"	type='text/css' media='screen, projection'> -->"+
					"    <script src='/siga/javascript/jquery.placeholder.js' type='text/javascript'></script>"+
					"<!--[if gte IE 5.5]><script language='JavaScript' src='/siga/javascript/jquery.ienav.js' type='text/javascript'></script><![endif]-->"+
					"<script type='text/javascript'>"+
					"	$(document).ready(function() {"+
					"		$('.links li code').hide();"+
					"		$('.links li p').click(function() {"+
					"			$(this).next().slideToggle('fast');"+
					"		});"+
					"		$('.once').click(function(e) {"+
					"			if (this.beenSubmitted)"+
					"				e.preventDefault();"+
					"			else"+
					"				this.beenSubmitted = true;"+
					"		});"+
					"		//$('.autogrow').css('overflow', 'hidden').autogrow();"+
					"	});"+
					"</script>"+
					"</head>"+
					"<body onload=''>"+
					"		<div class='gt-hd clearfix'>"+
					"			<!-- leaf watermark -->"+
					"			<div class='gt-leaf-watermark clearfix'>"+
					"				<!-- head top -->"+
					"				<div class='gt-hd-top clearfix'>"+
					"					<div class='gt-fixed-wrap clearfix'>"+
					"						<!-- utility box -->"+
					"						<!-- / utility box -->"+
					"						<!-- logo -->"+
					"						<a href='/siga' title='Página inicial' class='link-sem-estilo'>"+
					"							<div class='gt-logo' style='padding: 0;'>"+
					"								<img style='margin-top: 3px; margin-bottom: -13px;'"+
					"									src='/siga/imagens/logo.png'>"+
					"							</div>"+
					"							<div class='gt-company'>"+
					"								<strong> "+
					"								</strong>"+
					"							</div>"+
					"							<div class='gt-version'>"+
					"								Sistema Integrado de Gest&atilde;o Administrativa"+
					"								 - <span style='color: red'>SISTEMA"+
					"		 E "+
					"		BASE DE TESTES</span>"+
					"							</div>"+
					"							<!-- / logo -->"+
					"						</a>"+
					"					</div>"+
					"				</div>"+
					"				<!-- /head top -->"+
					"				<!-- navbar -->"+
					"					<div class='gt-navbar clearfix'>"+
					"						<div class='gt-fixed-wrap clearfix'>"+
					"							<!-- navigation -->"+
					"							<div class='gt-nav'>"+
					"								<ul id='navmenu-h'>"+
					"<li><a id='menu_siga' class='' href='#'>SIGA</a>"+
					"	<ul>"+
					"		<li><a href='/siga/principal.action'>Página Inicial</a>"+
					"		</li>"+
					"			<li><a href='#'>Módulos</a>"+
					"				<ul>"+
					"					<!-- <li><a href='/sigatr/'>Treinamento</a>"+
					"					</li> -->"+
					"					<!-- <li><a href='/SigaServicos/'>Serviços</a>"+
					"					</li> -->"+
					"				</ul>"+
					"			</li>"+
					"			<li><a href='#'>Administração</a>"+
					"				<ul>"+
					"					<li><a href='/siga/usuario/trocar_senha.action'>Trocar senha</a>"+
					"					</li>"+
					"						<li><a href='/siga/substituicao/listar.action'>Gerenciar possíveis substitutos</a>"+
					"						</li>"+
					"				</ul>"+
					"			</li>"+
					"		<li><a href='#'>Substituir</a>"+
					"			<ul class='navmenu-large'>"+
					"			</ul>"+
					"		</li>"+
					"		<li><a href='/siga/?GLO=true'>Logoff</a>"+
					"		</li>"+
					"	</ul>"+
					"</li>"+
					"<!-- insert menu -->"+
					"<!-- Não existe nenhum menu especial para a aplicação principal -->"+
					"								</ul>"+
					"							</div>"+
					"							<!-- / navigation -->"+
					"							<!-- search -->"+
					"						</div>"+
					"					</div>"+
					"			</div>"+
					"			<!-- /navbar -->"+
					"		</div>"+
					"		<!-- /leaf watermark -->"+
					"		</div>"+
					"		<div id='quadroAviso'"+
					"			style='position: absolute; font-weight: bold; padding: 4px; color: white; visibility: hidden'>-</div>"+
					"	<div id='carregando'"+
					"		style='position: absolute; top: 0px; right: 0px; background-color: red; font-weight: bold; padding: 4px; color: white; display: none'>Carregando...</div>"+
					"	<script type='text/javascript'>"+
					"		/*  converte para maiúscula a sigla do estado  */"+
					"		function converteUsuario(nomeusuario) {"+
					"			tmp = nomeusuario.value;"+
					"			if (tmp.match(re) || tmp.match(ret2)) {"+
					"				nomeusuario.value = tmp.toUpperCase();"+
					"			}"+
					"		}"+
					"	</script>"+
					"	<div class='gt-bd gt-cols clearfix'>"+
					"		<!-- main content -->"+
					"		<div id='gc-ancora' class='gt-content'>"+
					"			<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>"+
					"<h2 class='gt-table-head'>"+
					"	<b>Bem-vindo ao SIGA.</b>"+
					"</h2>"+
					"<h3 class='gt-table-head'>Veja abaixo as últimas novidades:</h3>"+
					"<h4>Novos Módulos</h4>"+
					"<p>O SIGA agora está integrado com os módulos do Siga SGP. "+
					"Foram integrados: Cadastro, Benefícios, AQ e Lotações (fase final de testes). "+
					"Podem ser acessados pelo menu, na seguinte opção: Módulos / Pessoas. "+
					"Os módulos estarão disponíveis de acordo com as permissões dos usuários logados. Em breve, mais novidades.</p>"+
					"<h4>Novo Design</h4>"+
					"<p>O SIGA apresenta, agora, um design muito mais simples e moderno."+
					"	Além das mudanças estéticas, também foram simplificadas algumas páginas"+
					"	e operações, o que beneficia principalmente as pessoas que utilizam"+
					"	muito o sistema.</p>"+
					"<h4>Modelos Padronizados</h4>"+
					"<p>Está sendo realizada uma padronização dos modelos de expedientes"+
					"	usados pelo CJF e pelos TRF's da 2ª e 3ª Região. Alguns modelos, como o"+
					"	de ofício, já estão com um novo layout no Siga-Doc.</p>"+
					"<h4>Novo Editor de Textos</h4>"+
					"<p>"+
					"	Siga-Doc possui um novo editor de textos, com mais recursos e menos"+
					"	problemas de formatação. Inicialmente, ele está disponível apenas em"+
					"	alguns modelos. Gradualmente, o antigo editor será substituído. <a"+
					"		href='/siga/arquivos/AjudaEditorTextosSigaDoc.pdf'><b>Veja instruções</b> </a> sobre"+
					"	o uso do editor."+
					"</p>"+
					"<h4>Busca Textual Integrada</h4>"+
					"<p>A busca de documentos por conteúdo agora pode ser feita na"+
					"	própria tela de pesquisa por filtros, por meio do campo Conteúdo.</p>"+
					"</font>"+
					"</table>"+
					"			<h4>Versão:  </h4>"+
					"		</div>"+
					"		<!-- / main content -->"+
					"		<!-- sidebar -->"+
					"		<div class='gt-sidebar'>"+
					"			<!-- login form head -->"+
					"			<div class='gt-mylogin-hd'>Identificação</div>"+
					"			<!-- login box -->"+
					"			<div class='gt-mylogin-box'>"+
					"				<!-- login form -->"+
					"				<form method='post' action='j_security_check' enctype='application/x-www-form-urlencoded' class='gt-form'>"+
					"					<!-- form row -->"+
					"					<div class='gt-form-row'>"+
					"						<label class='gt-label'>Matrícula</label> "+
					"						<input id='j_username' type='text' name='j_username' placeholder='XX99999'"+
					"							onblur='javascript:converteUsuario(this)' class='gt-form-text'>"+
					"					</div>"+
					"					<!-- /form row -->"+
					"					<!-- form row -->"+
					"					<div class='gt-form-row'>"+
					"						<label class='gt-label'>Senha</label> "+
					"						<input type='password' name='j_password' class='gt-form-text'>"+
					"					</div>"+
					"					<!-- /form row -->"+
					"					<!-- form row -->"+
					"					<div class='gt-form-row'>"+
					"						<input type='submit' value='Acessar' class='gt-btn-medium gt-btn-right'>"+
					"					</div>"+
					"					<!-- /form row -->"+
					"					<p class='gt-forgot-password'>"+
					"						<a href='/siga/usuario/incluir_usuario.action'>Sou um novo usuário</a>"+
					"					</p>"+
					"					<p class='gt-forgot-password'>"+
					"						<a href='/siga/usuario/esqueci_senha.action'>Esqueci minha senha</a>"+
					"					</p>"+
					"				</form>"+
					"				<!-- /login form -->"+
					"			</div>"+
					"			<!-- /login box -->"+
					"			<!-- Sidebar Navigation -->"+
					"			<div class='gt-sidebar-nav gt-sidebar-nav-blue'>"+
					"				<h3>Links Úteis</h3>"+
					"				<ul>"+
					"					<li><a href='/siga/arquivos/apostila_sigaex.pdf'>Apostila SIGA-Doc</a></li>"+
					"					<li><a href='/siga/arquivos/apostila_sigawf.pdf'>Apostila SIGA-Workflow</a></li>"+
					"				</ul>"+
					"			</div>"+
					"			<!-- /Sidebar Navigation -->"+
					"			<!-- Sidebar Content -->"+
					"		</div>"+
					"		<!-- / sidebar -->"+
					"	</div>"+
					"    <script type='text/javascript'>"+
					"        $('input, textarea').placeholder();"+
					"        $('#j_username').focus();"+
					"    </script>"+
					"</body>"+
					"</html>";
}
