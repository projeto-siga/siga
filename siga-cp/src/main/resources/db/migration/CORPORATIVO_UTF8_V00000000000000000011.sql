-------------------------------------------
--	SCRIPT:CONFIGURACAO
-------------------------------------------

--Alterações referentes a Configuração:
--   Corporativo (conforme hbm.xml) usava DT_INI_VIG_CONFIGURACAO 
--   e DT_FIM_VIG_CONFIGURACAO para guardar as informações do 
--   histórico. Após a alteração, as colunas usadas serão
--   HIS_DT_INI e HIS_DT_FIM, conforme o padrão. HIS_DT_INI
--   aproveitará a já existente e não utilizada DT_INI_REG.
--   E HIS_DT_FIM será criada. Após isso, os dados já existentes
--   nas DT_FIM_VIG_CONFIGURACAO e DT_INI_VIG_CONFIGURACAO serão
--   carregados nas novas colunas correspondentes. 

alter table CORPORATIVO.cp_configuracao rename column dt_ini_reg to his_dt_ini;
alter table CORPORATIVO.cp_configuracao add HIS_DT_FIM timestamp(6);
alter table CORPORATIVO.cp_configuracao add HIS_ID_INI NUMBER(19,0);
update CORPORATIVO.cp_configuracao set HIS_DT_INI = DT_INI_VIG_CONFIGURACAO;
update CORPORATIVO.cp_configuracao set HIS_DT_FIM = DT_FIM_VIG_CONFIGURACAO;
update CORPORATIVO.cp_configuracao set DT_INI_VIG_CONFIGURACAO = null;
update CORPORATIVO.cp_configuracao set DT_FIM_VIG_CONFIGURACAO = null;

/*CP_LOCALIDADE*/
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (1,'Angra dos Reis',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (2,'Barra do Piraí',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (3,'Cachoeiro do Itapemirim',8);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (4,'Campos',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (5,'Duque de Caxias',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (6,'Itaboraí',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (7,'Itaperuna',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (8,'Macaé',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (9,'Magé',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (10,'Niterói',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (11,'Nova Friburgo',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (12,'Nova Iguaçu',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (13,'Petrópolis',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (14,'Resende',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (15,'Rio de Janeiro',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (16,'São Gonçalo',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (17,'São João de Meriti',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (18,'São Mateus',8);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (19,'São Pedro da Aldeia',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (20,'Teresópolis',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (21,'Três Rios',19);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (22,'Vitória',8);
Insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (23,'Volta Redonda',19);


-- - - - - - - - - - - - - - - - - - - - - - 
-- - - CP_COMPLEXO - - - - - - - - - - - - - 
-- - - - - - - - - - - - - - - - - - - - - - 

	CREATE TABLE CORPORATIVO.CP_COMPLEXO (
         ID_COMPLEXO INT,
         NOME_COMPLEXO VARCHAR(100),
         ID_LOCALIDADE,
         FOREIGN KEY (ID_LOCALIDADE) REFERENCES CORPORATIVO.CP_LOCALIDADE(ID_LOCALIDADE)
       );
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (1, 'Almirante Barroso' 	,15);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (2, 'Angra' 				,1);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (3, 'Barra do Piraí' 	,2);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (4, 'Campos' 			,4);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (5, 'Duque de Caxias' 	,5);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (6, 'Equador'			,5);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (7, 'Friburgo' 			,11);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (8, 'Itaboraí' 			,6);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (9, 'Itaperuna' 			,7);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (10,'Macaé' 				,8);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (11,'Mage' 				,9);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (12,'Niteroi' 			,10);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (13,'Nova Iguaçu'		,12);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (14,'Petrópolis' 		,13);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (15,'Resende' 			,14);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (16,'Rio Branco'			,15);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (17,'Sao Gonçalo' 		,16);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (18,'Sao Joao de Meriti' ,17);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (19,'Sao Pedro da Aldeia',19);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (20,'São Cristovão' 		,15);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (21,'Teresópolis' 		,20);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (22,'Tres Rios' 			,21);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (23,'Venezuela' 			,15);
	insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade) values (24,'Volta Redonda' 		,23);
	

