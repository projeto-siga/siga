-------------------------------------------
--	SCRIPT:CORR SVC
-------------------------------------------

UPDATE CORPORATIVO.CP_SERVICO SET SIGLA_SERVICO = 'FS-SEC' WHERE sigla_servico = 'FS_SEC';
UPDATE CORPORATIVO.CP_SERVICO SET SIGLA_SERVICO = 'FS-GAB' WHERE sigla_servico = 'FS_GAB';
UPDATE CORPORATIVO.CP_SERVICO SET SIGLA_SERVICO = 'FS-JUIZ' WHERE sigla_servico = 'FS_JUIZ';

update CORPORATIVO.cp_configuracao set id_sit_configuracao = 1 where id_tp_configuracao = 202;

Insert into CORPORATIVO.CP_SERVICO (ID_SERVICO,SIGLA_SERVICO,DESC_SERVICO,ID_SERVICO_PAI,ID_TP_SERVICO) values (CORPORATIVO.CP_SERVICO_SEQ.NEXTVAL,'GOGLOBAL','Acesso ao Go-Global',null,2);

