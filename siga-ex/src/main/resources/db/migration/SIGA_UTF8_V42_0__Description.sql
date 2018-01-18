----------------------------------------------------------------------
--	SCRIPT: Renomeando Tipo de Movimentação Despachável para Inclusão
----------------------------------------------------------------------

update corporativo.cp_tipo_configuracao set dsc_tp_configuracao = 'Incluir como Filho' where id_tp_configuracao = 30;
