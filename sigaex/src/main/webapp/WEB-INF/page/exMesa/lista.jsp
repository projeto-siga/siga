<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>

<siga:pagina titulo="Mesa Virtual">

	<div id="app" class="container-fluid content">
		<c:if test="${not empty acessoAnteriorData}">
			<div class="row" id="row-bem-vindo">
				<div class="col">
					<p id="bem-vindo" class="alert alert-success mb-3 mb-0">Último
						acesso em ${acessoAnteriorData} no endereço
						${acessoAnteriorMaquina}.</p>
					<script>
						setTimeout(function() {
							$('#bem-vindo').fadeTo(1000, 0, function() {
								$('#row-bem-vindo').slideUp(1000);
							});
						}, 5000);
					</script>
				</div>
			</div>
		</c:if>

		<div class="row">
			<div class="col col-12 col-md-auto">
				<h2 class="mb-3"><i class="fa fa-file-alt"></i> Mesa Virtual</h2>
			</div>
			<div class="col my-1" style="vertical-align: text-bottom;"> 
				<span class="h-100  d-inline-block my-2" style="vertical-align: text-bottom;">
					<c:if test="${not empty visualizacao}"><b>(Delegante: ${visualizacao.titular.nomePessoa})</b></c:if> 
				</span> 
			</div> 
			<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos') && !ehPublicoExterno}">
				<div class="col col-12 col-sm-4 col-md-auto ml-md-auto mb-2">
					<a href="expediente/doc/editar" class="btn btn-success form-control"> <i class="fas fa-plus-circle mr-1"></i>
						<fmt:message key="documento.novo"/></a>
				</div>
				<div class="col col-12 col-sm-4 col-md-auto mb-2">
					<a href="expediente/doc/listar?primeiraVez=sim" class="btn btn-primary form-control">
						<i class="fas fa-search mr-1"></i><fmt:message key="documento.pesquisar"/> 
					</a>
				</div>
			</c:if>
			<div class="col col-12 col-sm-4 col-md-auto" v-if="carregando || (!errormsg &amp;&amp; filtrados.length >= 0)">
				<div class="input-group mb-3">
					<input type="text" class="form-control" placeholder="Filtrar" v-model="filtro" ng-model-options="{ debounce: 200 }">
				</div>
			</div>
			<div class="col col-sm-12" v-if="errormsg">
				<p class="alert alert-danger">
					<strong>Erro!</strong> {{errormsg}}
				</p>
			</div>
		</div>

		<div class="row d-print-none"
			v-if="!carregando &amp;&amp; lista.length > 0"></div>

		<div class="row mt-3" v-if="carregando &amp;&amp; primeiraCarga">
			<div class="col col-12">
				<p class="alert alert-warning">
					<strong>Aguarde,</strong> carregando documentos...
				</p>
			</div>
		</div>

		<div class="row mt-3"
			v-if="!errormsg && !carregando &amp;&amp; filtrados.length == 0">
			<div class="col col-12">
				<p class="alert alert-warning">
					<strong>Atenção!</strong> Nenhum documento na mesa.
				</p>
			</div>
		</div>
		
		<div class="row" v-if="filtrados.length > 0">
			<div class="col-sm-12">
				<table class="table table-sm table-borderless">
					<tbody>
						<template v-for="f in filtrados">
						<tr v-if="f.grupoExibir" class="table-group table-group-title alert alert-info align-middle">
							<th colspan="6" class="pb-0 pl-0">
								 <h5 class="mb-1 pl-2"><i :class="f.grupoIcone"></i> {{f.grupoNome}}</h5>
							</th>
						</tr>
						<tr v-if="f.grupoExibir" class="table-head">
							<th class="d-none d-md-block">Tempo</th>
							<th><fmt:message key = "usuario.mesavirtual.codigo"/></th>
							<th class="d-none d-md-block">Descrição</th>
							<c:if test="${siga_cliente == 'GOVSP'}">
								<th></th>
							</c:if>
							<th>Origem</th>
							<th class="d-none d-md-block"><fmt:message key = "usuario.mesavirtual.etiquetas"/></th>
							<th v-show="filtradosTemAlgumErro">Atenção</th>
						</tr>
						<tr v-bind:class="{odd: f.odd}">
							<td class="d-none d-md-block" :title="f.datahora">{{f.tempoRelativo}}</td>
							<td>
								<c:choose>
									<c:when test="${not empty idVisualizacao && idVisualizacao != 0}">
										<a :href="'expediente/doc/visualizar?sigla=' + f.codigo + '&idVisualizacao='+${idVisualizacao}">{{f.sigla}}</a>
									</c:when>
									<c:otherwise>
										<a :href="'expediente/doc/exibir?sigla=' + f.codigo">{{f.sigla}}</a>
									</c:otherwise>
								</c:choose>
								<span class="d-inline d-md-none"> - {{f.descr}}</span>
							</td>
							<td class="d-none d-md-block">{{f.descr}}</td>
							<c:if test="${siga_cliente == 'GOVSP'}">
								<td v-if="f.dataDevolucao == 'ocultar'"></td>
								<td v-if="f.dataDevolucao == 'alerta'"><i class="fa fa-exclamation-triangle text-warning"></i></td>
								<td v-if="f.dataDevolucao == 'atrasado'"><i class="fa fa-exclamation-triangle text-danger"></i></td>
							</c:if>
							<td>{{f.origem}}</td>
							<td class="d-none d-md-block" style="padding: 0;"><div
									class="xrp-label-container">
									<!-- class="list-unstyled blog-tags" -->
									<span v-for="m in f.list" :title="m.titulo"><button
											class="btn btn-default btn-sm xrp-label">
											<i :class="m.icone"></i> {{m.nome}}<span
												v-if="m.pessoa &amp;&amp; !m.daPessoa"> -
												{{m.pessoa}}</span><span
												v-if="m.unidade &amp;&amp; (!m.daLotacao || (!m.daPessoa && !m.deOutraPessoa))">
												/ {{m.unidade}}</span>
										</button></span>
								</div></td>
							<td v-show="filtradosTemAlgumErro" style="color: red">{{f.errormsg}}</td>
						</tr>
						<tr v-if="f.grupoEspacar" class="table-group table-group-separator">
							<th colspan="6" class="pb-2 pb-0 pl-0"></th>
						</tr>
						</template>
					</tbody>
				</table>
			</div>
		</div>
		<p class="alert alert-success"
			v-if="acessos &amp;&amp; acessos.length >= 1">Último acesso em
			{{acessos[1].datahora}} no endereço {{acessos[1].ip}}.</p>
	</div>

	<script type="text/javascript" src="../javascript/vue.min.js"></script>


	<script type="text/javascript" language="Javascript1.1">
	var httpGet = function(url, success, error) {  
		  var xhr = XMLHttpRequest ? new XMLHttpRequest() : 
		                             new ActiveXObject("Microsoft.XMLHTTP"); 
		  xhr.open("GET", url, true); 
		  xhr.onreadystatechange = function(){ 
		    if ( xhr.readyState == 4 ) { 
		      if ( xhr.status == 200 ) { 
		    success(xhr.responseText); 
		      } else { 
		    error(xhr, xhr.status); 
		      } 
		    } 
		  }; 
		  xhr.onerror = function () { 
		    error(xhr, xhr.status); 
		  }; 
		  xhr.send(); 
		} 
	var demo = new Vue({
		  el: "#app",

		  mounted: function() {
		    this.errormsg = undefined;
	      	var self = this
		    setTimeout(function() {
		      self.carregarMesa();
		    });
		  },

		  data: function() {
		    return {
		      mesa: undefined,
		      filtro: undefined,
		      lista: [],
		      todos: {},
		      carregando: false,
		      primeiraCarga: true,
		      acessos: [],
		      errormsg: undefined
		    };
		  },

		  computed: {
		    filtrados: function() {
		      var a = this.lista;
		      var grupo;
		      var odd = false;
		      a = this.filtrarPorSubstring(
		        a,
		        this.filtro,
		        "grupoNome,tempoRelativo,sigla,codigo,descr,origem,situacao,errormsg,list.nome,dataDevolucao".split(
		          ","
		        )
		      );
		      a = a.filter(function(item) {
		        return item.grupo !== "NENHUM";
		      });
		      for (var i = 0; i < a.length; i++) {
		        a[i].grupoExibir = a[i].grupo !== grupo;
		        grupo = a[i].grupo;
		        if (a[i].grupoExibir) odd = false;
		        if (a[i].grupoExibir && i > 0) a[i - 1].grupoEspacar = true;
		        odd = !odd;
		        a[i].odd = odd;
		      }
		      return a;
		    },

		    filtradosTemAlgumErro: function() {
		      if (!this.filtrados || this.filtrados.length === 0) return false;
		      for (var i = 0; i < this.filtrados.length; i++) {
		        if (this.filtrados[i].errormsg) return true;
		      }
		      return false;
		    }
		  },

		  methods: {
		    carregarMesa: function() {
		      this.carregando = true;
		      var erros = {};
		      if (this.lista && this.lista.length > 0) {
		        for (var i = 0; i < this.lista.length; i++) {
		          erros[this.lista[i].codigo] = this.lista[i].errormsg;
		        }
		      }
		      var self = this
		      httpGet('mesa.json?idVisualizacao=' + ${idVisualizacao}, function(text) {
		          self.carregando = false;
		          self.lista.length = 0;
		          var list = JSON.parse(text);
		          for (var i = 0; i < list.length; i++) {
		            list[i].errormsg = erros[list[i].codigo];
		            self.lista.push(self.fixItem(list[i]));
		          }
		          self.primeiraCarga = false;
		      }, function(xhr, status) {
		          self.carregando = false;
		          self.showError(xhr, self);
		      })
		    },

		    fixItem: function(item) {
		      this.applyDefauts(item, {
		        checked: false,
		        disabled: false,
		        grupo: undefined,
		        grupoNome: undefined,
		        grupoExibir: undefined,
		        grupoEspacar: undefined,
		        datahora: undefined,
		        datahoraFormatada: undefined,
		        sigla: undefined,
		        codigo: undefined,
		        descr: undefined,
		        origem: undefined,
		        situacao: undefined,
		        errormsg: undefined,
		        odd: undefined
		      });
		      if (item.datahora !== undefined) {
		        item.datahoraFormatada = this.formatJSDDMMYYYYHHMM(item.datahora);
		      }
		      return item;
		    },

		    // De UtilsBL
		    applyDefauts: function(obj, defaults) {
		      for (var k in defaults) {
		        if (!defaults.hasOwnProperty(k)) continue;
		        if (obj.hasOwnProperty(k)) continue;
		        obj[k] = defaults[k];
		      }
		    },

		    showError: function(error, component) {
		      component.errormsg = undefined;
		      try {
		      	component.errormsg = error.data.errormsg;
		      } catch (e) {}
		      if (
		        component.errormsg === undefined &&
		        error.statusText &&
		        error.statusText !== ""
		      ) {
		        component.errormsg = error.statusText;
		      }
		      if (component.errormsg === undefined) {
		        component.errormsg = "Erro desconhecido!";
		      }
		    },

		    // Filtra itens por uma string qualquer. Se desejar restringir o filtro apenas a algumas das propriedades,
		    // informar o parâmetro propriedades na forma: ['propriedade','outraPropriedade','lista.propriedade']
		    filtrarPorSubstring: function(a, s, propriedades) {
		      var re = new RegExp(s, "i");
		      return a.filter(function filterItem(item, idx, arr, context) {
		        for (var key in item) {
		          if (!item.hasOwnProperty(key)) continue;
		          var prop = context;
		          prop = context === undefined ? key : context + "." + key;
		          if (Array.isArray(item[key])) {
		            for (var i = 0; i < item[key].length; i++) {
		              if (filterItem(item[key][i], i, item[key], prop)) return true;
		            }
		          }
		          if (propriedades && propriedades.indexOf(prop) === -1) continue;
		          if (typeof item[key] === "string" && re.test(item[key])) {
		            return true;
		          }
		          if (
		            typeof item[key] === "object" &&
		            filterItem(item[key], 0, arr, prop)
		          ) {
		            return true;
		          }
		        }
		        return false;
		      });
		    },

		    formatJSDDMMYYYYHHMM: function(s) {
		      if (!s) return;
		      var r =
		        s.substring(8, 10) +
		        "/" +
		        s.substring(5, 7) +
		        "/" +
		        s.substring(0, 4) +
		        "&nbsp;" +
		        s.substring(11, 13) +
		        ":" +
		        s.substring(14, 16);
		      return r;
		    },

		    formatJSDDMMYYYY_AS_HHMM: function (s) {
		        if (s === undefined) return

		        var r = this.formatJSDDMMYYYYHHMM(s)
		        r = r.replace('&nbsp;', ' às ')
		        return r
		      },

		  }
		});
</script>
</siga:pagina>
