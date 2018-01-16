-------------------------------------------------
--	SCRIPT: Desmembramento de Capturados
-------------------------------------------------

Update SIGA.EX_TIPO_DOCUMENTO set DESCR_TIPO_DOCUMENTO = 'Externo Capturado' where ID_TP_DOC = 4;
Insert into SIGA.EX_TIPO_DOCUMENTO (ID_TP_DOC,DESCR_TIPO_DOCUMENTO) values (5,'Interno Capturado');
