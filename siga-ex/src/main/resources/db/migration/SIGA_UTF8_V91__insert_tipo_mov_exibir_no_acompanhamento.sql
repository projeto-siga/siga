--------------------------------------------------------------------------------------------------
--  Criação do Tipo de Movimentação: Disponibilizar ao Interessado 
--  Permite a Exibição do Documento no Acompanhamento do Protocolo
--------------------------------------------------------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=siga;
insert into siga.ex_tipo_movimentacao (id_tp_mov, DESCR_TIPO_MOVIMENTACAO) values (79, 'Disponibilizar no Acompanhamento do Protocolo');
