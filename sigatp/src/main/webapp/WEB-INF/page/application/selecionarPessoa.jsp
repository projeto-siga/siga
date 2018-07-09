<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

</br> <siga:selecao2 propriedade="filtro.lotaCondutorFiltro" tipo="lotacao" tema="simple" modulo="siga"/>
</br> <siga:pessoaLotaSelecao2 propriedadePessoa="filtro.condutorFiltro" propriedadeLotacao="filtro.lotaCondutorFiltro"/> 
</br> <siga:selecao2 propriedade="filtro.condutorFiltro" tipo="pessoa" tema="simple" modulo="siga"/> 
