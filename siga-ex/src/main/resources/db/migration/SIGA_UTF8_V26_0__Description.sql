insert into siga.ex_tipo_movimentacao values(58, 'Assinatura com senha');

insert into siga.ex_tipo_movimentacao values(59, 'Assinatura de movimentação com senha');

insert into siga.ex_tipo_movimentacao values(60, 'Conferência de Cópia de Documento com senha');

Insert into CORPORATIVO.CP_CONFIGURACAO (ID_CONFIGURACAO,HIS_DT_INI,ID_SIT_CONFIGURACAO,ID_TP_CONFIGURACAO) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.nextval,sysdate,'2','1');
Insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO_EX,ID_TP_MOV) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.currval,58);

Insert into CORPORATIVO.CP_CONFIGURACAO (ID_CONFIGURACAO,HIS_DT_INI,ID_SIT_CONFIGURACAO,ID_TP_CONFIGURACAO) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.nextval,sysdate,'2','1');
Insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO_EX,ID_TP_MOV) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.currval,59);

Insert into CORPORATIVO.CP_CONFIGURACAO (ID_CONFIGURACAO,HIS_DT_INI,ID_SIT_CONFIGURACAO,ID_TP_CONFIGURACAO) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.nextval,sysdate,'2','1');
Insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO_EX,ID_TP_MOV) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.currval,60);

insert into corporativo.cp_marcador(id_marcador, descr_marcador, ord_marcador, grupo_marcador)  values(62, 'Documento Assinado com Senha', 1, null);

insert into corporativo.cp_marcador(id_marcador, descr_marcador, ord_marcador, grupo_marcador) values(63, 'Movimentação Assinada com Senha', 1, null);

insert into corporativo.cp_marcador(id_marcador, descr_marcador, ord_marcador, grupo_marcador) values(64, 'Movimentação Conferida com Senha', 1, null);