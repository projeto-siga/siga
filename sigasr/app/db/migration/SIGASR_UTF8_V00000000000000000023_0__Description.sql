insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (24,'Campo Grande',19);
  
insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade, id_orgao_usu) values (27,'Campo Grande',24 , 1);

alter table sigasr.sr_resposta add 
(
	ID_MOVIMENTACAO	NUMBER(19,0),
	CONSTRAINT RESPOSTA_MOVIMENTACAO_FK FOREIGN KEY (ID_MOVIMENTACAO) REFERENCES SR_MOVIMENTACAO(ID_MOVIMENTACAO)
);
