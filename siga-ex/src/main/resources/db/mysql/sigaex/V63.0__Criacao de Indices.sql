CREATE INDEX iacs_cp_aplicacao_feriad_00001 
  ON corporativo.cp_aplicacao_feriado (id_lotacao) ; 

CREATE INDEX iacs_cp_marca_00002 
  ON corporativo.cp_marca (id_mobil) 
; 

CREATE INDEX iacs_cp_modelo_00001 
  ON corporativo.cp_modelo (his_idc_ini) 
; 

CREATE INDEX iacs_cp_modelo_00002 
  ON corporativo.cp_modelo (his_idc_fim) 
; 

CREATE INDEX iacs_ex_classificacao_00001 
  ON siga.ex_classificacao (his_idc_fim) 
; 

CREATE INDEX iacs_ex_classificacao_00002 
  ON siga.ex_classificacao (his_idc_ini) 
; 


CREATE INDEX iacs_ex_modelo_00001 
  ON siga.ex_modelo (his_idc_ini) ; 

CREATE INDEX iacs_ex_modelo_00002 
  ON siga.ex_modelo (his_idc_fim); 


CREATE INDEX iacs_ex_temporalidade_00001 
  ON siga.ex_temporalidade (his_idc_ini) 
; 

CREATE INDEX iacs_ex_temporalidade_00002 
  ON siga.ex_temporalidade (his_idc_fim) 
; 

CREATE INDEX iacs_ex_via_00001 
  ON siga.ex_via (his_idc_fim); 

CREATE INDEX iacs_ex_via_00002 
  ON siga.ex_via (his_idc_ini); 

