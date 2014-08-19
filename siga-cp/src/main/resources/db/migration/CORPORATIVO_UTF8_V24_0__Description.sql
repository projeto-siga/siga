-------------------------------------------
--	SCRIPT: ADICIONAR COLUNA LABEL_SERVICO
-------------------------------------------

alter table corporativo.cp_servico add (LABEL_SERVICO varchar2(35));

----------------------------------------------------------------
--	SCRIPT: ALTERA LABEL_SERVICO DA PÁGINA DE ACESSO A SERVICOS
----------------------------------------------------------------

update corporativo.cp_servico ser set ser.label_servico = '(K) RAIZ' where ser.sigla_servico = 'FS-RAIZ';
update corporativo.cp_servico ser set ser.label_servico = '(K) GABINETE' where ser.sigla_servico = 'FS-GAB';
update corporativo.cp_servico ser set ser.label_servico = '(K) SECRETARIA' where ser.sigla_servico = 'FS-SEC';
update corporativo.cp_servico ser set ser.label_servico = '(K) JUIZ' where ser.sigla_servico = 'FS-JUIZ';
update corporativo.cp_servico ser set ser.label_servico = '(K) PÚBLICA' where ser.sigla_servico = 'FS-PUB';
update corporativo.cp_servico ser set ser.label_servico = '(K) AUDIÊNCIA' where ser.sigla_servico = 'FS-AUD';
update corporativo.cp_servico ser set ser.label_servico = '(K) VIDEOCONF' where ser.sigla_servico = 'FS-VIDEO';