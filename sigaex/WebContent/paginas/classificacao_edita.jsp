<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>


<siga:pagina titulo="Classificação Documental">
<script type="text/javascript">
	function salvarClassificacao(){
		$('#frmClassificacao').submit();
	}
</script>

	<h2 class="gt-table-head">Classificação Documental</h2>
	<div class="gt-bd clearfix">
		<div class="gt-content">
			<h2 class="gt-form-head">Nova Classificação Documental</h2>

			<div class="gt-form gt-content-box">
				<div class="clearfix">
					<!-- left column -->
					<ww:form id="frmClassificacao" action="salvar" method="aSalvar">
						<div class="gt-left-col">
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Codificação</label> <input id="codificacao" name="codificacao" type="text" class="gt-form-text">
							</div>
							<!-- /form row -->
	
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Descrição</label> <input id="descrClassificacao" name="descrClassificacao" type="text" class="gt-form-text">
							</div>
							<!-- /form row -->
	
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Observação</label> <input id="obs" name="obs" type="text" class="gt-form-text">
							</div>
							<!-- /form row -->
						</div>
					</ww:form>
				</div>
				<!-- form row -->
				<div class="gt-form-row">
					<a id="btSalvar" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:salvarClassificacao()">Salvar</a>
					<p class="gt-cancel">
						ou <a href="">cancelar</a>
					</p>
				</div>
				<!-- /form row -->

			</div>
</siga:pagina>
